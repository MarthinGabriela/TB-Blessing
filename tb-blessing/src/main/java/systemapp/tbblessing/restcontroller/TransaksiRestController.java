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
    private ResponseEntity createTransaksi(@Valid @RequestBody TransaksiModel transaksi, BindingResult bindingResult) {
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

    @GetMapping(value = "/transaksi/view/{idTransaksi}")
    private TransaksiModel viewTransaksi(@PathVariable(value = "idTransaksi") Long idTransaksi) {
        try {
            return transaksiService.getTransaksiByIdTransaksi(idTransaksi);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Transaksi "+ String.valueOf(idTransaksi) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/transaksi/search/{namaPembeli}")
    private List<TransaksiModel> viewNamaTransaksi(@PathVariable(value = "namaPembeli") String namaPembeli) {
        try {
            return transaksiService.getTransaksiByNamaPembeli(namaPembeli);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tidak ada transaksi atas nama " + namaPembeli
            );
        }
    }

    @DeleteMapping(value = "/transaksi/{idTransaksi}")
    private ResponseEntity<String> deleteTransaksi(@PathVariable("idTransaksi") Long idTransaksi) {
        try {
            transaksiService.deleteTransaksi(idTransaksi);
            return ResponseEntity.ok("Delete Transaksi sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Transaksi "+ String.valueOf(idTransaksi) +" tidak valid"
            );
        }
    }
}