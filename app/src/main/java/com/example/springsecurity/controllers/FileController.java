package com.example.springsecurity.controllers;

import com.example.springsecurity.models.*;
import com.example.springsecurity.payload.DTO.ContainerDTO;
import com.example.springsecurity.payload.DTO.RepositoriesDTO;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.ContainerRepository;
import com.example.springsecurity.repository.RepositoriesRepository;
import com.example.springsecurity.repository.ResourcesRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.services.ResourcesStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test")
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    ResourcesStorageService resourcesStorageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    RepositoriesRepository repositoriesRepository;

    @Autowired
    ContainerRepository containerRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/repositories")
    @Transactional
    public List<Repositories> printRepositories() {
        return repositoriesRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/repositories/{repository_id}")
    @Transactional
    public Repositories printUserRepositories(@PathVariable UUID repository_id) {
        try {
            if(!repositoriesRepository.existsById(repository_id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Repository does not exist.");
            }
            return repositoriesRepository.findById(repository_id).get();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @GetMapping("/repositories/me")
    @Transactional
    public Set<Repositories> printMyRepositories(Authentication authentication) {
        try {
            return userRepository.findByUsername(authentication.getName()).get().getRepositories();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @GetMapping("/repositories/{repository_id}/containers")
    @Transactional
    public Set<Container> printContainers(@PathVariable UUID repository_id) {
        try {
            if(!repositoriesRepository.findById(repository_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Repository does not exist.");
            }
            Repositories repository = repositoriesRepository.findById(repository_id).get();
            return repository.getContainers();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @GetMapping("/repositories/{repository_id}/containers/{container_id}")
    @Transactional
    public Container printContainer(@PathVariable UUID repository_id, @PathVariable UUID container_id) {
        try {
            if(!repositoriesRepository.findById(repository_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Repository does not exist.");
            }

            if(!containerRepository.findById(container_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Container does not exist.");
            }
            return containerRepository.findById(container_id).get();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PostMapping("/repositories/{repository_id}")
    @Transactional
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable UUID repository_id, Authentication authentication) {
        try {
            User user = userRepository.findByUsername(authentication.getName()).get();
            Repositories repo;
            if (!repositoriesRepository.existsById(repository_id)) {
                repo = new Repositories("New repository");
                Set<Repositories> set = user.getRepositories();
                set.add(repo);
                user.setRepositories(set);
                repo.setUser(user);
                userRepository.save(user);
                repositoriesRepository.save(repo);
            } else {
                repo = repositoriesRepository.findById(repository_id).get();
            }

            resourcesStorageService.storeFile(file, user, repo, null);

            return ResponseEntity.ok(new MessageResponse("File has been uploaded successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Failed to upload files!"));
        }
    }

    @PostMapping("/repositories")
    @Transactional
    public ResponseEntity<?> createRepository(Authentication authentication, @RequestBody RepositoriesDTO dto ) {

        User user = userRepository.findByUsername(authentication.getName()).get();

        Repositories repo = new Repositories(dto.getName());

        Set<Repositories> userSet = user.getRepositories();
        userSet.add(repo);
        user.setRepositories(userSet);

        repo.setUser(user);

        userRepository.save(user);
        repositoriesRepository.save(repo);

        return ResponseEntity.ok(new MessageResponse("Repository has been created successfully."));
    }


    @PostMapping("/repositories/{repository_id}/containers")
    @Transactional
    public ResponseEntity<?> createContainer(@PathVariable UUID repository_id, @RequestBody ContainerDTO dto, Authentication authentication) {

            if (!repositoriesRepository.existsById(repository_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Repository does not exist."));
            }

            User user = userRepository.findByUsername(authentication.getName()).get();
            Repositories repo = repositoriesRepository.findById(repository_id).get();

            Container container = new Container(dto.getName());
            Set<Container> userSet = user.getContainers();
            userSet.add(container);
            user.setContainers(userSet);

            container.setUser(user);

            Set<Container> repoSet = repo.getContainers();
            repoSet.add(container);
            repo.setContainers(repoSet);

            container.setRepositories(repo);

            userRepository.save(user);
            repositoriesRepository.save(repo);
            containerRepository.save(container);

            return ResponseEntity.ok(new MessageResponse("Container has been created successfully."));
    }

    @PostMapping("/repositories/{repository_id}/containers/{container_id}")
    @Transactional
    public ResponseEntity<?> uploadFileToContainer(@RequestParam("file") MultipartFile file, @PathVariable UUID repository_id, @PathVariable UUID container_id, Authentication authentication) {
        try {
            if (!repositoriesRepository.existsById(repository_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Repository does not exist."));
            }

            Repositories repo = repositoriesRepository.findById(repository_id).get();

            User user = userRepository.findByUsername(authentication.getName()).get();
            Container container;

            if (!containerRepository.existsById(container_id)) {
                container = new Container("New container");
                Set<Container> userSet = user.getContainers();
                userSet.add(container);
                user.setContainers(userSet);

                container.setUser(user);

                Set<Container> repoSet = repo.getContainers();
                repoSet.add(container);
                repo.setContainers(repoSet);

                container.setRepositories(repo);

                userRepository.save(user);
                repositoriesRepository.save(repo);
                containerRepository.save(container);
            } else {
                container = containerRepository.findById(container_id).get();
            }

            resourcesStorageService.storeFile(file, user, repo, container);
            return ResponseEntity.ok(new MessageResponse("File has been uploaded successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Failed to upload files!"));
        }
    }

    @PatchMapping("/repositories/{repository_id}")
    @Transactional
    public ResponseEntity<?> editRepository(@PathVariable UUID repository_id, @RequestBody RepositoriesDTO dto) {
        try {
            if (!repositoriesRepository.existsById(repository_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Repository does not exist."));
            }

            Repositories repo = repositoriesRepository.findById(repository_id).get();
            repo.setName(dto.getName());
            repositoriesRepository.save(repo);

            return ResponseEntity.ok(new MessageResponse("Repository has been updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Failed to upload files!"));
        }
    }

    @PatchMapping("/repositories/{repository_id}/containers/{container_id}")
    @Transactional
    public ResponseEntity<?> editContainer(@PathVariable UUID repository_id, @RequestBody ContainerDTO dto, @PathVariable UUID container_id) {
        try {
            if (!repositoriesRepository.existsById(repository_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Repository does not exist."));
            }

            if (!containerRepository.existsById(container_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Container does not exist."));
            }

            Container container = containerRepository.findById(container_id).get();
            container.setContainer_name(dto.getName());
            containerRepository.save(container);

            return ResponseEntity.ok(new MessageResponse("Container has been updated successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse("Failed to upload files!"));
        }
    }

    @DeleteMapping("/repositories/{repository_id}")
    @Transactional
    public ResponseEntity<?> deleteRepositoryAdmin(@PathVariable UUID repository_id, Authentication authentication) {
        if (!repositoriesRepository.existsById(repository_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Repository does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        System.out.println(user.getRepositories());
        Set<Repositories> userSet = user.getRepositories();
        Set<Repositories> helperSet = new HashSet<>(); //cant modify current iterating list

        for(Repositories repo : userSet) {
            if(!repo.getId().equals(repository_id)) {
                helperSet.add(repo);
            }
        }
        user.setRepositories(helperSet);
        userRepository.save(user);
        repositoriesRepository.deleteById(repository_id);
        System.out.println(user.getRepositories());
        return ResponseEntity.ok(new MessageResponse("Repository is successfully deleted."));
    }


    @DeleteMapping("/repositories/{repository_id}/containers/{container_id}")
    @Transactional
    public ResponseEntity<?> deleteContainer(@PathVariable UUID repository_id, @PathVariable UUID container_id, Authentication authentication) {
        if (!repositoriesRepository.existsById(repository_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Repository does not exist."));
        }

        if (!containerRepository.existsById(container_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Container does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Repositories repo = repositoriesRepository.findById(repository_id).get();

        //remove container from user
        Set<Container> userSet = user.getContainers();
        Set<Container> helper1Set = new HashSet<>(); //cant modify current iterating list

        for(Container container : userSet) {
            if(!container.getId().equals(container_id)) {
                helper1Set.add(container);
            }
        }
        user.setContainers(helper1Set);
        userRepository.save(user);

        //remove container from repository
        Set<Container> repoSet = repo.getContainers();
        Set<Container> helper2Set = new HashSet<>();

        for(Container container : repoSet) {
            if(!container.getId().equals(container_id)) {
                helper2Set.add(container);

            }
        }
        repo.setContainers(helper2Set);
        repositoriesRepository.save(repo);

        containerRepository.deleteById(container_id);
        return ResponseEntity.ok(new MessageResponse("Deleted successfully."));
    }

    @DeleteMapping("/repositories/{repository_id}/{resource_id}")
    @Transactional
    public ResponseEntity<?> deleteFileFromRepository(@PathVariable UUID resource_id, @PathVariable UUID repository_id) {
        if (!repositoriesRepository.existsById(repository_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Repository does not exist."));
        }

        if (!resourcesRepository.existsById(resource_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource does not exist."));
        }
        Resources resource = resourcesRepository.findById(resource_id).get();

        if (!repositoriesRepository.findById(repository_id).get().getResources().contains(resource)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource not found in this repository"));
        }

        resourcesRepository.deleteById(resource_id);
        return ResponseEntity.ok(new MessageResponse("Your resource is successfully deleted."));
    }


    @DeleteMapping("/repositories/resources/{resource_id}")
    @Transactional
    public ResponseEntity<?> deleteFile(@PathVariable UUID resource_id) {

        if (!resourcesRepository.existsById(resource_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource does not exist."));
        }

        resourcesRepository.deleteById(resource_id);
        return ResponseEntity.ok(new MessageResponse("Your resource is successfully deleted."));
    }


    @GetMapping("/repositories/{repository_id}/resources/{resource_id}")
    public ResponseEntity<?> downloadFileFromRepository(@PathVariable UUID repository_id, @PathVariable UUID resource_id) {
        if (!repositoriesRepository.existsById(repository_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Repository does not exist."));
        }
        Repositories repo = repositoriesRepository.findById(repository_id).get();
        Set<Resources> set = repo.getResources();

        if (!resourcesRepository.findById(resource_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource does not exist."));
        }

        Resources resource = resourcesRepository.findById(resource_id).get();

        if (!set.contains(resource)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource not found in this repository."));
        }

        return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(resource.getContent_type()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +           resource.getResource_name() + "\"")
                        .body(new ByteArrayResource(resource.getData()));

    }

    @GetMapping("/repositories/{repository_id}/containers/{container_id}/resources/{resource_id}")
    public ResponseEntity<?> downloadFileFromContainer(@PathVariable UUID repository_id, @PathVariable UUID resource_id, @PathVariable UUID container_id) {
        if (!repositoriesRepository.existsById(repository_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Repository does not exist."));
        }

        if(!containerRepository.existsById(container_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Container does not exist."));
        }
        Repositories repo = repositoriesRepository.findById(repository_id).get();
        Container con = containerRepository.findById(container_id).get();
        Set<Resources> repositorySet = repo.getResources();
        Set<Resources> containerSet = con.getResources();

        if (!resourcesRepository.findById(resource_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource does not exist."));
        }

        Resources resource = resourcesRepository.findById(resource_id).get();

        if (!repositorySet.contains(resource)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource not found in this repository."));
        }

        if (!containerSet.contains(resource)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Resource not found in this container."));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContent_type()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getResource_name() + "\"")
                .body(new ByteArrayResource(resource.getData()));
    }

}