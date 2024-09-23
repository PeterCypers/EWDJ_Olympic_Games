package com.springBoot.examenTaak;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {
	
    @Autowired
    private LocaleResolver localeResolver;
	
	@GetMapping("/changeLocale")
    public String changeLocale(HttpServletRequest request) {
        localeResolver.setLocale(request, null, Locale.ENGLISH);
        return "redirect:/sports";
    }

}
