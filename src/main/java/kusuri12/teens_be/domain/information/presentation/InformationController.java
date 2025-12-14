package kusuri12.teens_be.domain.information.presentation;

import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInformationRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InformationListResponse;
import kusuri12.teens_be.domain.information.service.InformationService;
import kusuri12.teens_be.global.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/information")
@RequiredArgsConstructor
public class InformationController {

    private final InformationService infoArticleService;

    @GetMapping
    public ResponseEntity<List<InformationListResponse>> getAllInfoArticles(
            @RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return ResponseEntity.ok(infoArticleService.searchInfoArticles(keyword));
        }
        return ResponseEntity.ok(infoArticleService.getAllInfoArticles());
    }

    @GetMapping("/{information_Id}")
    public ResponseEntity<InformationDetailResponse> getInfoArticleDetail(
            @PathVariable Long information_Id) {
        return ResponseEntity.ok(infoArticleService.getInfoArticleDetail(information_Id));
    }

    @PostMapping
    public ResponseEntity<Void> createInfoArticle(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody CreateInformationRequest request) {
        Long userId = authDetails.getId();
        infoArticleService.createInfoArticle(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{information_Id}")
    public ResponseEntity<Void> updateInfoArticle(
            @PathVariable Long information_Id,
            @RequestBody UpdateInformationRequest request) {
        infoArticleService.updateInfoArticle(information_Id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{information_Id}")
    public ResponseEntity<Void> deleteInfoArticle(@PathVariable Long information_Id) {
        infoArticleService.deleteInfoArticle(information_Id);
        return ResponseEntity.noContent().build();
    }
}