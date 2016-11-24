package com.mkyong.web.token;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/token")
public class TokenAjaxController {
    // @ResponseBody, not necessary, since class is annotated with @RestController
    // @RequestBody - Convert the json data into object (SearchCriteria) mapped by field name.
    // @JsonView(Views.Public.class) - Optional, limited the json data display to client.
    @RequestMapping(value = "/acquire")
    public String getSearchResultViaAjax() {
        return TokenUtil.generateTokenKey();
    }
}
