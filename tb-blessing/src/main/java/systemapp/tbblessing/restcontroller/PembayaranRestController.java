package systemapp.tbblessing.restcontroller;

import systemapp.tbblessing.model.*;
import systemapp.tbblessing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class PembayaranRestController {
    @Autowired
    private PembayaranService pembayaranService;

    @PostMapping(value = "/pembayaran")
    private ResponseEntity createPembayaran(@Valid @RequestBody PembayaranModel pembayaran, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field pembayaran tidak lengkap");
        } else {
            pembayaranService.addPembayaran(pembayaran);
            return ResponseEntity.ok("Add Pembayaran sukses");
        }
    }

    @GetMapping(value = "/list-pembayaran")
    private List<PembayaranModel> viewListPembayaran() {
        return pembayaranService.getAllPembayaran();
    }

    @GetMapping(value = "/pembayaran/update/{idPembayaran}")
    private ResponseEntity updatePembayaran(@PathVariable(value = "idPembayaran") Long idPembayaran, @RequestBody PembayaranModel pembayaran) {
        try {
            pembayaranService.updatePembayaran(idPembayaran, pembayaran);
            return ResponseEntity.ok("Update Pembayaran sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Pembayaran "+ String.valueOf(idPembayaran) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/pembayaran/view/{idPembayaran}")
    private PembayaranModel viewPembayaran(@PathVariable(value = "idPembayaran") Long idPembayaran) {
        try {
            return pembayaranService.getPembayaranByIdPembayaran(idPembayaran);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Pembayaran "+ String.valueOf(idPembayaran) +" tidak valid"
            );
        }
    }

    @DeleteMapping(value = "/pembayaran/{idPembayaran}")
    private ResponseEntity<String> deletePembayaran(@PathVariable("idPembayaran") Long idPembayaran) {
        try {
            pembayaranService.deletePembayaran(idPembayaran);
            return ResponseEntity.ok("Delete Pembayaran sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Pembayaran "+ String.valueOf(idPembayaran) +" tidak valid"
            );
        }
    }
}