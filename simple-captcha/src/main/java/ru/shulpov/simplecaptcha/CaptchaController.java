package ru.shulpov.simplecaptcha;

import com.github.cage.Cage;
import com.github.cage.GCage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.io.IOException;

@Controller
public class CaptchaController {

    private static final Logger log = LoggerFactory.getLogger(CaptchaController.class);

    // Контроллер для генерации капчи и отображения изображения
    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Генерация капчи
        Cage cage = new GCage();
        String captchaText = cage.getTokenGenerator().next();

        // Сохранение капчи в сессию для последующей проверки
        request.getSession().setAttribute("captcha", captchaText);

        // Генерация изображения капчи
        // Устанавливаем content type как изображение PNG
        request.setAttribute("captchaText", captchaText); // Записываем текст капчи в атрибут для проверки
        log.info("Captcha: [captcha_text = '{}']", captchaText);
        // Отправляем картинку в ответ
        response.setContentType("image/png");
        cage.draw(captchaText, response.getOutputStream());
    }

    //http://localhost:8080/validateCaptcha?captcha=blablatext
    @PostMapping("/validateCaptcha")
    public String validateCaptcha(@RequestParam("captcha") String userCaptcha, Model model, HttpServletRequest request) {
        // Извлекаем правильный ответ из сессии
        String correctCaptcha = (String) request.getSession().getAttribute("captcha");

        // Проверяем, совпадает ли введённый пользователем текст с правильным ответом
        if (correctCaptcha != null && correctCaptcha.equalsIgnoreCase(userCaptcha)) {
            model.addAttribute("message", "Captcha Passed!");
        } else {
            model.addAttribute("message", "Captcha Failed!");
        }

        return "resultPage"; // Страница с результатом проверки капчи
    }

    @GetMapping("/captchaPage")
    public String captchaPage() {

        return "captchaPage";
    }
}