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
public class TransaksiRestController {
    @Autowired
    private TransaksiService transaksiService;

    @PostMapping(value = "/transaksi")
    private ResponseEntity createBarang(@Valid @RequestBody TransaksiModel transaksi, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field transaksi tidak lengkap");
        } else {
            transaksiService.addTransaksi(transaksi);
            return ResponseEntity.ok("Add Transaksi sukses");
        }
    }

    @GetMapping(value = "/list-transaksi")
    private List<TransaksiModel> viewListTransaksi() {
        return transaksiService.getAllTransaksi();
    }

    @GetMapping(value = "/transaksi/update/{idTransaksi}")
    private ResponseEntity updateTransaksi(@PathVariable(value = "idTransaksi") Long idTransaksi, @RequestBody TransaksiModel transaksi) {
        try {
            transaksiService.updateTransaksi(idTransaksi, transaksi);
            return ResponseEntity.ok("Update Transaksi sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Transaksi "+ String.valueOf(idTransaksi) +" tidak valid"
            );
        }
    }

    
}
