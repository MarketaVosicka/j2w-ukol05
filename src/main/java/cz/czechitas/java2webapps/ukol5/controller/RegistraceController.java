package cz.czechitas.java2webapps.ukol5.controller;

import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Kontroler obsluhující registraci účastníků dětského tábora.
 */
@Controller
@RequestMapping("")
public class RegistraceController {

    @GetMapping("/")
    public ModelAndView form() {
        ModelAndView modelAndView = new ModelAndView("formular");
        modelAndView.addObject("formular", new RegistraceForm());
        return modelAndView;
    }

    @PostMapping("/")
    public Object form(@ModelAttribute("formular")
                       @Valid RegistraceForm form, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            return "/formular";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate narozeni = LocalDate.parse(form.getDatumNarozeni(), formatter);
        Period period = narozeni.until(LocalDate.now());
        int vek = period.getYears();

        if (vek <= 9 || vek >= 15 || form.getSporty().size() < 2) {
            if (vek <= 9 || vek >= 15) {
                bindingResult.rejectValue("datumNarozeni", "", "Pro účast je nutné, aby dítě bylo mezi 9 a 15 lety (včetně)");
                return "/formular";
            } else if (form.getSporty().size() < 2) {
                bindingResult.rejectValue("sporty", "", "Je nutné zadat alespoň 2 sporty");
                return "/formular";
            }
        }

        return new ModelAndView("/rekapitulace")
                .addObject("jmeno", form.getJmeno())
                .addObject("prijmeni", form.getPrijmeni())
                .addObject("datumNarozeni", form.getDatumNarozeni())
                .addObject("pohlavi", form.getPohlavi())
                .addObject("sporty", form.getSporty())
                .addObject("turnus", form.getTurnus())
                .addObject("email", form.getEmail())
                .addObject("telefon", form.getTelefon());

    }
}
