package kusuri12.teens_be.domain.information.controller;

import kusuri12.teens_be.domain.information.dto.InfoArticleDto;
import kusuri12.teens_be.domain.information.service.InfoArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/information")
@RequiredArgsConstructor
public class InfoArticleController {

    private final InfoArticleService infoArticleService;

    @GetMapping
    public ResponseEntity<List<InfoArticleDto.InfoArticleListResponse>> getAllInfoArticles() {
        return ResponseEntity.ok(infoArticleService.getAllInfoArticles());
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<InfoArticleDto.InfoArticleDetailResponse> getInfoArticleDetail(@PathVariable Long articleId) {
        return ResponseEntity.ok(infoArticleService.getInfoArticleDetail(articleId));
    }

    @PostMapping
    public ResponseEntity<Void> createInfoArticle(@RequestBody InfoArticleDto.CreateInfoArticleRequest request) {
        infoArticleService.createInfoArticle(request);
        return ResponseEntity.ok().build();
    }
}