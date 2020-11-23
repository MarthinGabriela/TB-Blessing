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
public class BarangRestController {
    @Autowired
    private BarangService barangService;

    @PostMapping(value = "/barang")
    private ResponseEntity createBarang(@Valid @RequestBody BarangModel barang, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field barang tidak lengkap");
        } else {
            barangService.addBarang(barang);
            return ResponseEntity.ok("Add Barang sukses");
        }
    }

    @GetMapping(value = "/list-barang")
    private List<BarangModel> viewListBarang() {
        return barangService.getAllBarang();
    }

    @GetMapping(value = "/list-barang/less/{kuantitas}")
    private List<BarangModel> viewListbarang(@PathVariable(value = "kuantitas") Long kuantitas) {
        return barangService.getByStockBarangLessThanEqual(kuantitas);
    }

    @GetMapping(value = "/barang/update/{idBarang}")
    private ResponseEntity updateBarang(@PathVariable(value = "idBarang") Long idBarang, @RequestBody BarangModel barang) {
        try {
            barangService.updateBarang(idBarang, barang);
            return ResponseEntity.ok("Update Barang sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Barang "+ String.valueOf(idBarang) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/barang/view/{idBarang}")
    private BarangModel viewBarang(@PathVariable(value = "idBarang") Long idBarang) {
        try {
            return barangService.getBarangByIdBarang(idBarang);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Barang "+ String.valueOf(idBarang) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/barang/search/{nama}")
    private BarangModel viewNamaBarang(@PathVariable(value = "nama") String nama) {
        try {
            return barangService.getBarangByNamaBarang(nama);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Barang " + nama +" tidak valid"
            );
        }
    }

    // Jika perlu Hapus barang, kemungkinan sih enggak kepake tapi jaga-jaga aja
    @DeleteMapping(value = "/barang/{idBarang}")
    private ResponseEntity<String> deleteBarang(@PathVariable("idBarang") Long idBarang) {
        try {
            barangService.deleteBarang(idBarang);
            return ResponseEntity.ok("Delete Barang sukses");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Barang "+ String.valueOf(idBarang) +" tidak valid"
            );
        }
    }
}
