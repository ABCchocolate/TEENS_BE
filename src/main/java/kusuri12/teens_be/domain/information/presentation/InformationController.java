package kusuri12.teens_be.domain.information.presentation;

import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationListResponse;
import kusuri12.teens_be.domain.information.service.InformationService;
import kusuri12.teens_be.global.security.userdetails.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/information")
@RequiredArgsConstructor
public class InformationController {

    private final InformationService infoArticleService;

    @GetMapping
    public ResponseEntity<Page<InformationListResponse>> getAllInfoArticles(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(required = false) String keyword) {

        int pageIndex = Math.max(page, 1) - 1;
        Pageable pageable = PageRequest.of(pageIndex, 10);

        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(infoArticleService.searchInfoArticles(keyword, pageable));
        }
        return ResponseEntity.ok(infoArticleService.getAllInfoArticles(pageable));
    }

    @GetMapping("/{information_id}")
    public ResponseEntity<InformationDetailResponse> getInfoArticleDetail(
            @PathVariable Long information_id) {
        return ResponseEntity.ok(infoArticleService.getInfoArticleDetail(information_id));
    }

    @PostMapping
    public ResponseEntity<Void> createInfoArticle(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody CreateInformationRequest request) {
        Long userId = authDetails.getId();
        infoArticleService.createInfoArticle(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{information_id}")
    public ResponseEntity<Void> updateInfoArticle(
            @PathVariable(name = "information_id") Long informationId,
            @RequestBody UpdateInformationRequest request) {
        infoArticleService.updateInfoArticle(informationId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{information_id}")
    public ResponseEntity<Void> deleteInfoArticle(
            @PathVariable(name = "information_id") Long informationId) {
        infoArticleService.deleteInfoArticle(informationId);
        return ResponseEntity.noContent().build();
    }
}