package com.example.springsecurity.security.services;

import com.example.springsecurity.Exceptions.FileStorageException;
import com.example.springsecurity.Exceptions.MyFileNotFoundException;

import com.example.springsecurity.models.Container;
import com.example.springsecurity.models.Repositories;
import com.example.springsecurity.models.Resources;
import com.example.springsecurity.models.User;
import com.example.springsecurity.repository.ContainerRepository;
import com.example.springsecurity.repository.RepositoriesRepository;
import com.example.springsecurity.repository.ResourcesRepository;
import com.example.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ResourcesStorageService {

    @Autowired
    private ResourcesRepository resourcesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RepositoriesRepository repositoriesRepository;

    @Autowired
    ContainerRepository containerRepository;

    @Transactional
    public void storeFile(MultipartFile file, User user, Repositories repo, Container container) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Resources resources = new Resources(fileName, file.getContentType(), file.getBytes());

            Set set = repo.getResources();
            set.add(resources);

            List list = user.getResources();
            list.add(resources);

            if (container != null) {
                Set<Resources> containerSet = container.getResources();
                containerSet.add(resources);
                container.setResources(containerSet);
                containerRepository.save(container);
                System.out.println(container.getResources());
            }

            repo.setResources(set);
            user.setResources(list);

            resources.setSize(file.getSize());
            resources.setUser(user);
            resources.setRepositories(repo);
            resources.setContainer(container);

            repositoriesRepository.save(repo);
            userRepository.save(user);

            resourcesRepository.save(resources);

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}