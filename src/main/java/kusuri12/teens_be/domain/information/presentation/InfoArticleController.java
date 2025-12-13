package kusuri12.teens_be.domain.information.presentation;

import kusuri12.teens_be.domain.information.presentation.dto.request.CreateInfoArticleRequest;
import kusuri12.teens_be.domain.information.presentation.dto.request.UpdateInfoArticleRequest;
import kusuri12.teens_be.domain.information.presentation.dto.response.InfoArticleDetailResponse;
import kusuri12.teens_be.domain.information.presentation.dto.response.InfoArticleListResponse;
import kusuri12.teens_be.domain.information.service.InfoArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info-article")
@RequiredArgsConstructor
public class InfoArticleController {

    private final InfoArticleService infoArticleService;

    @GetMapping
    public ResponseEntity<List<InfoArticleListResponse>> getAllInfoArticles() {
        return ResponseEntity.ok(infoArticleService.getAllInfoArticles());
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<InfoArticleDetailResponse> getInfoArticleDetail(
            @PathVariable Long articleId) {
        return ResponseEntity.ok(infoArticleService.getInfoArticleDetail(articleId));
    }

    @PostMapping
    public ResponseEntity<Void> createInfoArticle(
            @RequestBody CreateInfoArticleRequest request) {
        infoArticleService.createInfoArticle(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Void> updateInfoArticle(
            @PathVariable Long articleId,
            @RequestBody UpdateInfoArticleRequest request) {
        infoArticleService.updateInfoArticle(articleId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteInfoArticle(@PathVariable Long articleId) {
        infoArticleService.deleteInfoArticle(articleId);
        return ResponseEntity.noContent().build();
    }
}