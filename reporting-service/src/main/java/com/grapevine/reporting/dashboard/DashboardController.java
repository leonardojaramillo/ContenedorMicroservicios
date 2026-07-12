package com.grapevine.reporting.dashboard;

import com.grapevine.reporting.dashboard.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getMetrics() {
        return dashboardService.getMetrics();
    }
}