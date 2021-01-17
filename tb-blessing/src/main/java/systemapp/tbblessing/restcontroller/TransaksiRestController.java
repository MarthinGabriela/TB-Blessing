package systemapp.tbblessing.restcontroller;

import systemapp.tbblessing.model.*;
import systemapp.tbblessing.object.BarangJualInput;
import systemapp.tbblessing.object.BarangReturInput;
import systemapp.tbblessing.object.BaseResponse;
import systemapp.tbblessing.object.TransaksiInput;
import systemapp.tbblessing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class TransaksiRestController {
    @Autowired
    private TransaksiService transaksiService;

    @Autowired
    private BarangService barangService;

    @Autowired
    private BarangJualService barangJService;

    @Autowired
    private BarangReturService barangRService;

    @Autowired
    private PembayaranService pembayaranService;

    @PostMapping(value = "/transaksi")
    private BaseResponse createTransaksi(@Valid @RequestBody TransaksiInput input) {
        TransaksiModel transaksi = transaksiConverter(input);
        transaksiService.addTransaksi(transaksi);
        BarangModel barang = new BarangModel();
        BarangJualModel barangJ = new BarangJualModel();
        BarangReturModel barangR = new BarangReturModel();

        for(BarangJualInput barangJual : input.getListBarangJual()) {

            try {
                barang = barangService.getBarangByNamaBarang(barangJual.getNamaBarang());
            } catch(NoSuchElementException e) {
                continue;
            }

                barangJ.setHargaJual(barangJual.getHarga());
                barangJ.setStockBarangJual(barangJual.getStock());
                barangJ.setBarangModel(barangService.getBarangByNamaBarang(barangJual.getNamaBarang()));
                barangJ.setTransaksiModel(transaksi);
                barangJService.addBarang(barangJ);
                transaksi.addListBarangJual(barangJ);

                barang.setStockBarang(barang.getStockBarang() - barangJ.getStockBarangJual());
                barangService.updateBarang(barang.getIdBarang(), barang);

        }

        for(BarangReturInput barangRetur : input.getListBarangRetur()) {

            try {
                barang = barangService.getBarangByNamaBarang(barangRetur.getNamaBarang());
            } catch(NoSuchElementException e) {
                continue;
            }

            barangR.setHargaRetur(barangRetur.getHarga());
            barangR.setStockBarangRetur(barangRetur.getStock());
            barangR.setBarangModel(barangService.getBarangByNamaBarang(barangRetur.getNamaBarang()));
            barangR.setTransaksiModel(transaksi);
            barangRService.addBarang(barangR);
            transaksi.addListBarangRetur(barangR);

            barang.setStockBarang(barang.getStockBarang() + barangR.getStockBarangRetur());
            barangService.updateBarang(barang.getIdBarang(), barang);
        }

        PembayaranModel firstPayment = new PembayaranModel();

        firstPayment.setPembayaran(input.getDP());
        firstPayment.setTanggalPembayaran(new Date());
        firstPayment.setTransaksiModel(transaksi);
        pembayaranService.addPembayaran(firstPayment);
        transaksi.addListPembayaran(firstPayment);

        TransaksiModel updatedTransaksi = transaksiService.updateNominalTransaksi(transaksi);
        updatedTransaksi = transaksiService.updateHutangTransaksi(transaksi);
        
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setMessage("Add Transaksi Sukses");
        response.setResult(updatedTransaksi);

        return response;
    }

    @GetMapping(value = "/list-transaksi")
    private BaseResponse viewListTransaksi() {
        List<TransaksiModel> list = transaksiService.getAllTransaksi();
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setMessage("List Transaksi");
        response.setResult(list);

        return response;
    }

    @GetMapping(value = "/transaksi/update/{idTransaksi}")
    private BaseResponse updateTransaksi(@PathVariable(value = "idTransaksi") Long idTransaksi, @RequestBody TransaksiModel input) {
        try {
            
            TransaksiModel transaksi = transaksiService.updateTransaksi(idTransaksi, input);

            BarangModel barang = new BarangModel();
            BarangJualModel barangJ = new BarangJualModel();
            BarangReturModel barangR = new BarangReturModel();

            for(BarangJualInput barangJual : input.getListBarangJual()) {

                try {
                    barang = barangService.getBarangByNamaBarang(barangJual.getNamaBarang());
                } catch(NoSuchElementException e) {
                    continue;
                }

                    barangJ.setHargaJual(barangJual.getHarga());
                    barangJ.setStockBarangJual(barangJual.getStock());
                    barangJ.setBarangModel(barangService.getBarangByNamaBarang(barangJual.getNamaBarang()));
                    barangJ.setTransaksiModel(transaksi);
                    barangJService.addBarang(barangJ);
                    transaksi.addListBarangJual(barangJ);

                    barang.setStockBarang(barang.getStockBarang() - barangJ.getStockBarangJual());
                    barangService.updateBarang(barang.getIdBarang(), barang);
            }

            for(BarangReturInput barangRetur : input.getListBarangRetur()) {

                try {
                    barang = barangService.getBarangByNamaBarang(barangRetur.getNamaBarang());
                } catch(NoSuchElementException e) {
                    continue;
                }

                barangR.setHargaRetur(barangRetur.getHarga());
                barangR.setStockBarangRetur(barangRetur.getStock());
                barangR.setBarangModel(barangService.getBarangByNamaBarang(barangRetur.getNamaBarang()));
                barangR.setTransaksiModel(transaksi);
                barangRService.addBarang(barangR);
                transaksi.addListBarangRetur(barangR);

                barang.setStockBarang(barang.getStockBarang() + barangR.getStockBarangRetur());
                barangService.updateBarang(barang.getIdBarang(), barang);
            }

            PembayaranModel firstPayment = new PembayaranModel();

            firstPayment.setPembayaran(input.getDP());
            firstPayment.setTanggalPembayaran(new Date());
            firstPayment.setTransaksiModel(transaksi);
            pembayaranService.addPembayaran(firstPayment);
            transaksi.addListPembayaran(firstPayment);

            TransaksiModel updatedTransaksi = transaksiService.updateNominalTransaksi(transaksi);
            updatedTransaksi = transaksiService.updateHutangTransaksi(transaksi);
            
            BaseResponse response = new BaseResponse();
            response.setStatus(200);
            response.setMessage("Add Transaksi Sukses");
            response.setResult(updatedTransaksi);

            return response;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Transaksi "+ String.valueOf(idTransaksi) + " tidak valid"
            );
        }
    }

    @GetMapping(value = "/transaksi/view/{idTransaksi}")
    private BaseResponse viewTransaksi(@PathVariable(value = "idTransaksi") Long idTransaksi) {
        try {
            BaseResponse response = new BaseResponse();
            response.setStatus(200);
            response.setMessage("Update Transaksi Sukses");
            response.setResult(transaksiService.getTransaksiByIdTransaksi(idTransaksi));
            return response;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Error Id Transaksi "+ String.valueOf(idTransaksi) +" tidak valid"
            );
        }
    }

    @GetMapping(value = "/transaksi/search/{namaPembeli}")
    private BaseResponse viewNamaTransaksi(@PathVariable(value = "namaPembeli") String namaPembeli) {
        try {
            BaseResponse response = new BaseResponse();
            response.setStatus(200);
            response.setMessage("Update Transaksi Sukses");
            response.setResult(transaksiService.getTransaksiByNamaPembeli(namaPembeli));
            return response;
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

    private TransaksiModel transaksiConverter(TransaksiInput input) {
        TransaksiModel transaksi = new TransaksiModel();
        transaksi.setAlamat(input.getAlamat());
        transaksi.setDiskon(input.getDiskon());
        transaksi.setNamaPembeli(input.getNamaPembeli());
        transaksi.setTanggalTransaksi(new Date());
        transaksi.setHutangTransaksi(0L);
        transaksi.setNominalTransaksi(0L);
        transaksi.setListBarangJual(new ArrayList<BarangJualModel>());
        transaksi.setListBarangRetur(new ArrayList<BarangReturModel>());
        transaksi.setListPembayaran(new ArrayList<PembayaranModel>());
        return transaksi;
    }
}