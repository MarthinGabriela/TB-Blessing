package systemapp.tbblessing.restcontroller;

import systemapp.tbblessing.model.*;
import systemapp.tbblessing.object.BaseInput;
import systemapp.tbblessing.object.BaseResponse;
import systemapp.tbblessing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/barang")
    private BaseResponse createBarang(@Valid @RequestBody BaseInput input) {
        try {
            loginService.getLoginByToken(input.getToken()).get();
        } catch (NoSuchElementException e) {
            return new BaseResponse(401, "Input Token is not available in the Database");
        }

        BarangModel barang = (BarangModel) input.getInput();
        
    }

    @GetMapping(value = "/list-barang")
    private BaseResponse viewListBarang() {
        List<BarangModel> list = barangService.getAllBarang();
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setMessage("List Barang Get");
        response.setResult(list);

        return response;
    }

    @GetMapping(value = "/list-barang/less/{kuantitas}")
    private BaseResponse viewListbarang(@PathVariable(value = "kuantitas") Long kuantitas) {
        List<BarangModel> list = barangService.getByStockBarangLessThanEqual(kuantitas);
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setMessage("List Barang Less Get " + kuantitas);
        response.setResult(list);

        return response;
    }

    @PutMapping(value = "/barang/update/{idBarang}")
    private BaseResponse updateBarang(@PathVariable(value = "idBarang") Long idBarang, @RequestBody BarangModel barang) {
        try {
            BarangModel update = barangService.updateBarang(idBarang, barang);
            BaseResponse response = new BaseResponse();
            response.setStatus(200);
            response.setMessage("Update barang Id " + idBarang + " sukses");
            response.setResult(update);

            return response;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Barang "+ String.valueOf(idBarang) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/barang/view/{idBarang}")
    private BaseResponse viewBarang(@PathVariable(value = "idBarang") Long idBarang) {
        try {
            BarangModel barang = barangService.getBarangByIdBarang(idBarang);
            BaseResponse response = new BaseResponse();
            response.setStatus(200);
            response.setMessage("Get Barang");
            response.setResult(barang);

            return response;
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
