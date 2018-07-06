package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UploadController{
    // Save the upload file to this folder
    private static String UPLOADED_FOLDER = "D://_temp//upload//";

    @GetMapping("/")
    public String index(){
        return "upload";
    }

    @GetMapping("/mu")
    public String indexMulti(){
        return "multiUpload";
    }

    @PostMapping("/upload") // new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes){
        if (file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "请选择一个文件进行上传");
        }

        try{
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message", "成功上传文件：‘" + file.getOriginalFilename() + "’");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @PostMapping("/multiUpload")
    public String multiUpload(@RequestParam("files") MultipartFile[] files,
                              RedirectAttributes redirectAttributes){
        if(files.equals(null)){
            redirectAttributes.addFlashAttribute("message", "请选择至少一个文件进行上传");
        }

        if(files!=null){
            String messages = "成功上传文件：‘";
            for (int i = 0; i < files.length; i++){
                MultipartFile file = files[i];
                try{
                    // Get the file and save it somewhere
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                    Files.write(path, bytes);

                    messages += file.getOriginalFilename() + "’,‘";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            redirectAttributes.addFlashAttribute("message", messages);
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus(){
        return "uploadStatus";
    }
}