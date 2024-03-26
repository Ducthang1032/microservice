package ndt.project.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.service.AESService;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.MetaData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class DecryptEnCryptController {
    private final AESService aesService;

    @SneakyThrows
    @GetMapping("/encrypt")
    public ResponseEntity<ResponseMetaData> encryptData(@RequestParam(value = "data") String data) {

        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), aesService.encryptData(data)));
    }

    @SneakyThrows
    @GetMapping("/decrypt")
    public ResponseEntity<ResponseMetaData> decryptData(@RequestParam(value = "data") String data) {

        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), aesService.decryptData(data)));
    }

    @SneakyThrows
    @GetMapping("/encrypt-without-url")
    public ResponseEntity<ResponseMetaData> encryptDataWithoutUrl(@RequestParam(value = "data") String data) {

        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), aesService.encryptWithoutUrl(data)));
    }

    @SneakyThrows
    @GetMapping("/decrypt-without-url")
    public ResponseEntity<ResponseMetaData> decryptDataWithoutUrl(@RequestPart(value = "data") String data) {

        return ResponseEntity.status(MetaData.SUCCESS.getMetaCode())
                .body(new ResponseMetaData(new MetaDTO(MetaData.SUCCESS), aesService.decryptDataWithoutUrl(data)));
    }

}
