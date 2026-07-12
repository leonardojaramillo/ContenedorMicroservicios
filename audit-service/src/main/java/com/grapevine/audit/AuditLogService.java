package com.grapevine.audit;

import com.grapevine.audit.dto.AuditLogResponse;
import com.grapevine.audit.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository  auditLogRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public void record(CreateAuditLogRequest request) {
        AuditLog log = AuditLog.builder()
                .action(request.getAction())
                .description(request.getDescription())
                .performedBy(request.getPerformedBy() != null ? request.getPerformedBy() : "Sistema")
                .performedByEmail(currentUserEmailOrNull())
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }

    public List<AuditLogResponse> findAll(String action, String from, String to) {

        Criteria criteria = new Criteria();
        boolean hasCriteria = false;

        if (action != null && !action.isBlank()) {
            criteria = criteria.and(new Criteria("action").is(action));
            hasCriteria = true;
        }

        if (from != null && !from.isBlank()) {
            LocalDateTime rangeStart = LocalDate.parse(from).atStartOfDay();
            criteria = hasCriteria
                    ? criteria.and(new Criteria("createdAt").greaterThanEqual(rangeStart))
                    : new Criteria("createdAt").greaterThanEqual(rangeStart);
            hasCriteria = true;
        }

        if (to != null && !to.isBlank()) {
            LocalDateTime rangeEnd = LocalDate.parse(to).atTime(23, 59, 59);
            criteria = hasCriteria
                    ? criteria.and(new Criteria("createdAt").lessThanEqual(rangeEnd))
                    : new Criteria("createdAt").lessThanEqual(rangeEnd);
            hasCriteria = true;
        }

        CriteriaQuery query = hasCriteria ? new CriteriaQuery(criteria) : new CriteriaQuery(new Criteria());
        query.addSort(Sort.by(Sort.Direction.DESC, "createdAt"));

        SearchHits<AuditLog> hits = elasticsearchTemplate.search(query, AuditLog.class);

        return hits.stream()
                .map(SearchHit::getContent)
                .map(this::toResponse)
                .toList();
    }

    private String currentUserEmailOrNull() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return principal instanceof String ? (String) principal : null;
        } catch (Exception e) {
            return null;
        }
    }

    private AuditLogResponse toResponse(AuditLog l) {
        return AuditLogResponse.builder()
                .id(l.getId())
                .action(l.getAction())
                .description(l.getDescription())
                .performedBy(l.getPerformedBy())
                .performedByEmail(l.getPerformedByEmail())
                .createdAt(l.getCreatedAt())
                .build();
    }
}