package com.example.nuestro.services;

import com.example.nuestro.entities.File;
import com.example.nuestro.models.files.FileResponse;
import com.example.nuestro.repositories.FileRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.hibernate.boot.archive.spi.ArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService
{
    private static final Logger logger= LoggerFactory.getLogger(PostService.class);
    private  final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileResponse GetData(String id){
        logger.info("User is getting file {}",id);

        var file= fileRepository.findById(id).orElseThrow(()-> new ArchiveException("File not found"));

        return new FileResponse(file);
    }

    public File Get(String id) throws NuestroException {
        logger.info("User is getting file {}",id);

        return fileRepository.findById(id)
                .orElseThrow(()-> new NuestroException("File not found"));
    }

    public  FileResponse UploadFile(MultipartFile multipartFile) throws NuestroException, IOException {
        var file =Upload(multipartFile);
        return  new FileResponse(file);
    }
    public File Upload(MultipartFile multipartFile) throws IOException, NuestroException {
        logger.info("User is uploading file {}", multipartFile.getName());

        if(multipartFile.isEmpty())
            throw  new NuestroException("File is empty");
        //Get user context?
        var existingFile= fileRepository.findByNameAndContentTypeAndSizeInBytes(multipartFile.getOriginalFilename()
                ,multipartFile.getContentType(), multipartFile.getSize());
        if(existingFile!=null){
            logger.info("File {} already exists", existingFile.getId());
            //return existingFile;
        }
        var file= new File(multipartFile.getOriginalFilename(),multipartFile.getContentType(),multipartFile.getBytes(),multipartFile.getSize());
        fileRepository.save(file);
        return  file;
    }

    public  FileResponse Update(String id, MultipartFile file) throws NuestroException, IOException {
        logger.info("User is updating file {}", id);

        var existingFile= fileRepository.findById(id)
                .orElseThrow(()-> new NuestroException("File not found"));

        existingFile.Update(file);

        fileRepository.save(existingFile);
        logger.info("File saved successfully");
        return  new FileResponse(existingFile);
    }
}
