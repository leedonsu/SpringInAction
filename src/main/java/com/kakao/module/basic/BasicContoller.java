package com.kakao.module.basic;

import net.daum.tenth2.Tenth2OutputStream;
import net.daum.tenth2.util.Tenth2Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Controller
public class BasicContoller {

    private static String domain = "http://twg.beta.tset.daumcdn.net";

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/upload-requestPart")
    public String upload(Model model, @RequestPart("file") byte[] file) throws Exception {
        String path = getPath();
        Tenth2Util.put(path, file, Tenth2OutputStream.Option.CPLACE);
        model.addAttribute("result",
                String.format("<img src=\"%s\"/>",domain + path));
        return "success";
    }

    @RequestMapping("/upload-MultipartFile")
    public String upload(Model model, @RequestParam("file") MultipartFile file) throws Exception {
        String path = getPath();
        Tenth2Util.put(path, file.getBytes(), Tenth2OutputStream.Option.CPLACE);
        model.addAttribute("result",
                String.format("<img src=\"%s\"/><br/>original file name:%s<br/>size:%d",
                        domain + path,
                        file.getOriginalFilename(),
                        file.getSize()));
        return "success";
    }

    private String getPath() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return "/test/ellie/spring/" + timestamp.getTime();
    }
}
