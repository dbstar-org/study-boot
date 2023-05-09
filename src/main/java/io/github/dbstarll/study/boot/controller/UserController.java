package io.github.dbstarll.study.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dbstarll.study.boot.model.SummaryWithTotal;
import io.github.dbstarll.study.boot.utils.PageQuery;
import io.github.dbstarll.study.entity.enums.Module;
import io.github.dbstarll.study.service.PrincipalService;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('MODE_ADMIN')")
class UserController {
    @Autowired
    private PrincipalService principalService;
    @Autowired
    private ObjectMapper mapper;

    @GetMapping
    SummaryWithTotal<ObjectNode> index(final PageQuery query) {
        final Bson filter = null;
        // TODO 实现按PageQuery分页
        return SummaryWithTotal.warp(principalService.count(filter),
                principalService.findWithExerciseBook(filter, Module.ENGLISH).map(t -> {
                    final ObjectNode principal = mapper.convertValue(t.getKey(), ObjectNode.class);
                    if (t.getValue() != null) {
                        principal.set("exercise", mapper.convertValue(t.getValue(), ObjectNode.class));
                    }
                    return principal;
                })).query(query);
    }
}
