package com.grapevine.inventory.transfer;

import com.grapevine.inventory.transfer.dto.CreateTransferGuideRequest;
import com.grapevine.inventory.transfer.dto.IncidentRequest;
import com.grapevine.inventory.transfer.dto.TransferGuideResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer-guides")
@RequiredArgsConstructor
public class TransferGuideController {

    private final TransferGuideService transferGuideService;

    @GetMapping
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public List<TransferGuideResponse> findAll() {
        return transferGuideService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse findById(@PathVariable Long id) {
        return transferGuideService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse create(@RequestBody CreateTransferGuideRequest request) {
        return transferGuideService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse update(@PathVariable Long id,
                                        @RequestBody CreateTransferGuideRequest request) {
        return transferGuideService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transferGuideService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/prepare")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse prepare(@PathVariable Long id) {
        return transferGuideService.prepare(id);
    }

    @PatchMapping("/{id}/dispatch")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse dispatch(@PathVariable Long id) {
        return transferGuideService.dispatch(id);
    }

    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse deliver(@PathVariable Long id) {
        return transferGuideService.deliver(id);
    }

    @PatchMapping("/{id}/incident")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse reportIncident(@PathVariable Long id,
                                                @RequestBody IncidentRequest request) {
        return transferGuideService.reportIncident(id, request);
    }

    @PatchMapping("/{id}/resume")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse resume(@PathVariable Long id) {
        return transferGuideService.resume(id);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public TransferGuideResponse cancel(@PathVariable Long id) {
        return transferGuideService.cancel(id);
    }
}