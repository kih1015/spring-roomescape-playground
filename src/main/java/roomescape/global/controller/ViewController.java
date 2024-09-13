package roomescape.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/reservation")
    public String getReservationPage() {
        return "new-reservation";
    }

    @GetMapping("/time")
    public String getTimePage() {
        return "time";
    }
}
