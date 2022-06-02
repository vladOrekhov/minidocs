package com.example.minidocsservice.controllers;

import com.example.minidocsservice.entity.Document;
import com.example.minidocsservice.entity.DocumentVersion;
import com.example.minidocsservice.entity.RegCard;
import com.example.minidocsservice.repository.FileStoreRepository;
import com.example.minidocsservice.repository.FileVersionRepository;
import com.example.minidocsservice.repository.RegCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    private FileStoreRepository fileStoreRepository;
    @Autowired
    private FileVersionRepository fileVersionRepository;
    @Autowired
    private RegCardRepository regCardRepository;


    @GetMapping(value = "/uploadpage")
    public String uploadPageController( Model model){
        model.addAttribute("date", new Timestamp(System.currentTimeMillis()));
        return "uploadpage";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String handleFileUpload(@RequestParam("documentName") String name,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("documentIntroNumber") String introNum,
                                   @RequestParam("author") String author,
                                   RedirectAttributes redirectAttributes) {
        if (name.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Имя не может быть пустым!");
            return "redirect:/uploadpage";
        }
        if (introNum.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Входящий номер не может быть пустым!");
            return "redirect:/uploadpage";
        }
        if (author.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Автор не может быть пустым!");
            return "redirect:/uploadpage";
        }
        if (!file.isEmpty()) {
            Document newDoc = new Document();
            newDoc.setDocument_name(name);
            newDoc.setAuthor(author);
            fileStoreRepository.saveAndFlush(newDoc);

            DocumentVersion newDocVersion = new DocumentVersion();
            newDocVersion.setDocument_version_id(1);
            newDocVersion.setDocument_id(newDoc.getDocument_id());
            newDocVersion.setVersion_author(author);
            try {
                newDocVersion.setContent(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileVersionRepository.saveAndFlush(newDocVersion);

            RegCard regCard = new RegCard();
            regCard.setDocumentId(newDoc.getDocument_id());
            regCard.setDocumentIntroNumber(introNum);
            regCard.setDateIntro(new Timestamp(System.currentTimeMillis()));

            regCardRepository.saveAndFlush(regCard);

        } else {
            redirectAttributes.addFlashAttribute("message", "Выберите файл!");
            return "redirect:/uploadpage";
        }
        redirectAttributes.addFlashAttribute("message", "Вы успешно загрузили файл " + name + "!");
        return "redirect:/uploadpage";
    }

    @GetMapping(value = "/newversion/{document_id}")
    public String newVersionController(@PathVariable("document_id") Integer document_id, Model model){
        model.addAttribute("date", new Timestamp(System.currentTimeMillis()));
        model.addAttribute("document_name", fileStoreRepository.findById(document_id).get().getDocument_name());
        model.addAttribute("document_id", document_id);

        return "newversion";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/uploadVersion/{document_id}")
    public String newVersionUploadController(@PathVariable("document_id") Integer document_id,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("author") String author,
                                             RedirectAttributes redirectAttributes){
        if (author.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Автор не может быть пустым!");
            return "redirect:/newversion/{document_id}";
        }
        if (!file.isEmpty()) {
        DocumentVersion newDocVersion = new DocumentVersion();
        newDocVersion.setDocument_version_id(fileVersionRepository.findLatestVersion(document_id)+1);
        newDocVersion.setDocument_id(document_id);
        newDocVersion.setVersion_author(author);
        try {
            newDocVersion.setContent(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
            fileVersionRepository.saveAndFlush(newDocVersion);
            redirectAttributes.addFlashAttribute("message", "Вы успешно загрузили файл!");
            return "redirect:/newversion";

        } else {
            redirectAttributes.addFlashAttribute("message", "Выберите файл!");
            return "redirect:/newversion/{document_id}";
        }
    }



    @GetMapping(value = "/removepage/{document_id}")
    public String removePageController(@PathVariable("document_id") Integer document_id,
                                       Model model
                                       ){
        model.addAttribute("date", new Timestamp(System.currentTimeMillis()));
        model.addAttribute("documentName", fileStoreRepository.findById(document_id).get().getDocument_name());
        return "removepage";
    }

    @RequestMapping(method = RequestMethod.POST,value = "/remove/{document_id}")
    public String removeController(@PathVariable("document_id") Integer document_id,
                                   @RequestParam ("documentExternNumber") String documentExternNumber,
                                   RedirectAttributes redirectAttributes){
        if (documentExternNumber.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Исходящий номер не может быть пустым!");
            return "redirect:/removepage/{document_id}";
        }
        regCardRepository.remove(new Timestamp(System.currentTimeMillis()), Integer.valueOf(documentExternNumber), document_id);
        redirectAttributes.addFlashAttribute("message", "Вы успешно сняли файл с учета!");
        return "redirect:/removepage";
    }

    @GetMapping(value = "/downloadpage/{document_id}")
    public String downloadPageController(@PathVariable("document_id") Integer document_id, Model model){
        Optional<RegCard> regCard = regCardRepository.findByDocumentId(document_id);
        model.addAttribute("document_id", document_id);
        model.addAttribute("date", regCard.get().getDateIntro());
        model.addAttribute("documentName", fileStoreRepository.findById(document_id).get().getDocument_name());
        model.addAttribute("documentIntroNumber",regCard.get().getDocumentIntroNumber());
        model.addAttribute("listOfVersions", fileVersionRepository.findAllVersions(document_id));
        return "downloadpage";
    }

    @RequestMapping(method = RequestMethod.POST,value = "/download/{document_id}")
    public String downloadController(@PathVariable("document_id") Integer document_id,
                                     @RequestParam("document_version") Integer document_version,
                                     RedirectAttributes redirectAttributes){
        DocumentVersion file = fileVersionRepository.findDocumentVersionsByDocument_version_idAndDocument_id(document_id,document_version);
        String home = System.getProperty("user.home");
        File downloadedFile = new File(home+"/Downloads/" + fileStoreRepository.findById(document_id).get().getDocument_name() + "_" + document_version + ".txt");
        writeByte(file.getContent(),downloadedFile);
        redirectAttributes.addFlashAttribute("message", "Файл "+downloadedFile.getName()+" успешно загружен и находится в папке Downloads!");
        return "redirect:/downloadpage/{document_id}";

    }

    static void writeByte(byte[] bytes, File file)
    {
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String provideUploadInfo(Model model) {
        List<Document> stillActive = new LinkedList<>();
        List<Document> notActive = new LinkedList<>();

        for(Document document: fileStoreRepository.findAll()){
            if(!(regCardRepository.checkExterned(document.getDocument_id())>0)){
                stillActive.add(document);
            } else {
                notActive.add(document);
            }
        }

        model.addAttribute("activeDocuments", stillActive);
        model.addAttribute("inactiveDocuments", notActive);

        return "index";
    }
}
