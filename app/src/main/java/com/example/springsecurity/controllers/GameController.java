package com.example.springsecurity.controllers;

import com.example.springsecurity.models.*;
import com.example.springsecurity.payload.DTO.*;
import com.example.springsecurity.payload.Projections.PathsProjection;
import com.example.springsecurity.payload.response.GameResponse;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.*;
import com.example.springsecurity.security.services.ResourcesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")

public class GameController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ResourcesStorageService resourcesStorageService;

    @Autowired
    ResourcesRepository resourcesRepository;

    @Autowired
    RepositoriesRepository repositoriesRepository;

    @Autowired
    GamesRepository gamesRepository;

    @Autowired
    ScenesRepository scenesRepository;

    @Autowired
    AnnotationsRepository annotationsRepository;

    @Autowired
    QuestionsRepository questionsRepository;

    @Autowired
    ScenePointsRepository scenepointsRepository;

    @Autowired
    PathsRepository pathsRepository;

    @Autowired
    GameRegistrationsRepository gameRegistrationsRepository;

    @Autowired
    MovesRepository movesRepository;

    @Autowired
    AnnotationAnswersRepository annotationAnswersRepository;

    @Autowired
    AnswersRepository answersRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/games")
    @Transactional
    public List<Games> printGames() {
        return gamesRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/me")
    @Transactional
    public Set<Games> printMyGames(Authentication authentication) {
        try {
            User user = userRepository.findByUsername(authentication.getName()).get();
            return user.getGames();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}")
    @Transactional
    public Games printGame(@PathVariable UUID game_id, Authentication authentication) {
        try {
            if(!gamesRepository.existsById(game_id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game does not exist.");
            }

            User user = userRepository.findByUsername(authentication.getName()).get();
            Games game = gamesRepository.findById(game_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: You can't view someone else's game");
            }

            return game;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games")
    @Transactional
    public ResponseEntity<?> createGame(@RequestBody GamesDTO dto, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).get();

        if (!resourcesRepository.findById(dto.getImage_id()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid image id."));
        }
        Resources resource = resourcesRepository.findById(dto.getImage_id()).get();

        Games game = new Games(dto.getTitle(), dto.getDescription(), resource);
        resource.setGames1(game);
        resourcesRepository.save(resource);

        Set<Games> userSet = user.getGames();
        userSet.add(game);
        user.setGames(userSet);
        game.setUser(user);

        userRepository.save(user);
        gamesRepository.save(game);
        System.out.println(user.getGames());
        return ResponseEntity.ok(new MessageResponse("Game has been created successfully."));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PatchMapping("/games/{game_id}")
    @Transactional
    public ResponseEntity<?> editGame(@RequestBody GamesDTO dto, @PathVariable UUID game_id, Authentication authentication) {
                if (!gamesRepository.findById(game_id).isPresent()) {
                    return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid game id."));
                }
                Games game = gamesRepository.findById(game_id).get();
                User user = userRepository.findByUsername(authentication.getName()).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You can't edit someone else's game."));
        }

                if(dto.getTitle() != null) {
                    game.setTitle(dto.getTitle());
                }

                if(dto.getCreate_story() != null) {
                    game.setCreate_story(dto.getCreate_story());
                }

                if(dto.getDescription() != null) {
                    game.setDescription(dto.getDescription());
                }

                if(dto.getEnds() != null) {
                    game.setEnds(dto.getEnds());
                }

                if(dto.getIs_public() != null) {
                    game.setIs_public(dto.getIs_public());
                }

                if(dto.getGame_mode() != null) {
                    game.setGame_mode(dto.getGame_mode());
                }

                if(dto.getMax_skips() != null) {
                    game.setMax_skips(dto.getMax_skips());
                }

                if(dto.getParticipants_limit() != null) {
                    game.setParticipants_limit(dto.getParticipants_limit());
                }

                if(dto.getRegistrations_ends() != null) {
                    game.setRegistrations_ends(dto.getRegistrations_ends());
                }

                if(dto.getRegistrations_start() != null) {
                    game.setRegistrations_start(dto.getRegistrations_start());
                }

                if(dto.getStarts() != null) {
                    game.setStarts(dto.getStarts());
                }

                if(dto.getStory_cover_colour() != null) {
                    game.setStory_cover_colour(dto.getStory_cover_colour());
                }

                if(dto.getStory_cover_text_colour() != null) {
                    game.setStory_cover_text_colour(dto.getStory_cover_text_colour());
                }

                if(dto.getTerms() != null) {
                    game.setTerms(dto.getTerms());
                }

                if(dto.getTerms() != null) {
                    game.setTerms(dto.getTerms());
                }

                if(dto.getImage_id() != null) {
                    if (!resourcesRepository.findById(dto.getImage_id()).isPresent()) {
                        return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Error: Invalid image id."));
                    }
                    game.setImage_id(resourcesRepository.findById(dto.getImage_id()).get());
                    Resources resource = resourcesRepository.findById(dto.getImage_id()).get();
                    resource.setGames1(game);
                    resourcesRepository.save(resource);
                }

                if(dto.getStory_image_id() != null) {
                    if (!resourcesRepository.findById(dto.getStory_image_id()).isPresent()) {
                        return ResponseEntity
                                .badRequest()
                                .body(new MessageResponse("Error: Invalid image id."));
                    }
                    game.setStory_image_id(resourcesRepository.findById(dto.getStory_image_id()).get());
                    Resources resource = resourcesRepository.findById(dto.getStory_image_id()).get();
                    resource.setGames2(game);
                    resourcesRepository.save(resource);
                }

                gamesRepository.save(game);
                return ResponseEntity.ok(new MessageResponse("Your game is successfully updated."));

        }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/games")
    @Transactional
    public ResponseEntity<?> deleteGames() {
        gamesRepository.deleteAll();
        return ResponseEntity.ok(new MessageResponse("All games have been deleted."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}")
    @Transactional
    public ResponseEntity<?> deleteGame (@PathVariable UUID game_id, Authentication authentication) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist"));
        }

        Games games = gamesRepository.findById(game_id).get();
        User user = userRepository.findByUsername(authentication.getName()).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(games)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You can't delete someone else's game."));
        }

        System.out.println(user.getGames());

        //remove game from user
        Set<Games> userSet = user.getGames();
        Set<Games> helperSet = new HashSet<>(); //cant modify current iterating list

        for(Games game : userSet) {
            if(!game.getId().equals(game_id)) {
                helperSet.add(game);
            }
        }

        user.setGames(helperSet);
        userRepository.save(user);
        gamesRepository.deleteById(game_id);
        System.out.println(user.getGames());
        return ResponseEntity.ok(new MessageResponse("Game have been deleted."));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/scenes")
    @Transactional
    public Set<Scenes> printGameScenes(@PathVariable UUID game_id, Authentication authentication) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game does not exist.");
        }
        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't view someone else's game!");
        }

        return game.getScenes();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games/{game_id}/scenes")
    @Transactional
    public ResponseEntity<?> postGameScene(@PathVariable UUID game_id, Authentication authentication,  @RequestBody ScenesDTO dto) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't create a scene in someone else's game!");
        }

        Scenes scene = new Scenes(dto.getTitle(), dto.getInstruction());

        Set<Scenes> gameSet = game.getScenes();
        gameSet.add(scene);
        game.setScenes(gameSet);
        scene.setGames(game);

        gamesRepository.save(game);
        scenesRepository.save(scene);
        return ResponseEntity.ok(new MessageResponse("Scene has been created successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PatchMapping("/games/{game_id}/scenes/{scene_id}")
    @Transactional
    public ResponseEntity<?> editGameScene(@RequestBody ScenesDTO dto, @PathVariable UUID game_id, Authentication authentication, @PathVariable UUID scene_id) {

        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid game id."));
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid scene id."));
        }

        Games game = gamesRepository.findById(game_id).get();
        User user = userRepository.findByUsername(authentication.getName()).get();
        Scenes scene = scenesRepository.findById(scene_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You can't edit a scene in someone else's game!"));
        }

        if(dto.getDescription() != null) {
            scene.setDescription(dto.getDescription());
        }

        if(dto.getHelp_message() != null) {
            scene.setHelp_message(dto.getHelp_message());
        }

        if(dto.getId_mapping() != null) {
            scene.setId_mapping(dto.getId_mapping());
        }

        if(dto.getImage_upload() != null) {
            scene.setImage_upload(dto.getImage_upload());
        }

        if(dto.getInstruction() != null) {
            scene.setInstruction(dto.getInstruction());
        }

        if(dto.getIs_skippable() != null) {
            scene.setIs_skippable(dto.getIs_skippable());
        }

        if(dto.getScene_position() != null) {
            scene.setScene_position(dto.getScene_position());
        }

        if(dto.getStory_extra_help() != null) {
            scene.setStory_extra_help(dto.getStory_extra_help());
        }

        if(dto.getStory_instruction() != null) {
            scene.setStory_instruction(dto.getStory_instruction());
        }

        if(dto.getStory_page_background() != null) {
            scene.setStory_page_background(dto.getStory_page_background());
        }

        if(dto.getStory_success_message() != null) {
            scene.setStory_success_message(dto.getStory_success_message());
        }

        if(dto.getStory_text_colour() != null) {
            scene.setStory_text_colour(dto.getStory_text_colour());
        }

        if(dto.getSuccess_message() != null) {
            scene.setSuccess_message(dto.getSuccess_message());
        }

        if(dto.getTitle() != null) {
            scene.setTitle(dto.getTitle());
        }

        if(dto.getVideo_upload() != null) {
            scene.setVideo_upload(dto.getVideo_upload());
        }

        /*if(dto.getAnnotation_id() != null) {

            if (!annotationsRepository.findById(dto.getAnnotation_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid annotation id."));
            }

            Annotations annotation = annotationsRepository.findById(dto.getAnnotation_id()).get();
            scene.setAnnotation_id(annotation);
            annotation.setScene(scene);
            annotation.setScene_id(scene.getId());
            annotationsRepository.save(annotation);

        }*/

        if(dto.getQuestion_id() != null) {

            if (!questionsRepository.findById(dto.getQuestion_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid question id."));
            }

            Questions question = questionsRepository.findById(dto.getQuestion_id()).get();
            scene.setQuestion_id(question);
            question.setScenes(scene);
            questionsRepository.save(question);
        }

        if(dto.getScene_points_id() != null) {

            if (!scenepointsRepository.findById(dto.getScene_points_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid scene points id."));
            }

            ScenePoints scenePoint = scenepointsRepository.findById(dto.getScene_points_id()).get();

            scene.setScene_points_id(scenePoint);
            scenePoint.setScenes(scene);
            scenepointsRepository.save(scenePoint);
            System.out.println(scenePoint.getScenes().getId());
        }

        if(dto.getScene_location_radius_unit() != null) {
            scene.setScene_location_radius_unit(dto.getScene_location_radius_unit());
        }

        if(dto.getScene_location_radius() != null) {
            scene.setScene_location_radius(dto.getScene_location_radius());
        }

        if(dto.getScene_location_lat() != null) {
            scene.setScene_location_lat(dto.getScene_location_lat());
        }

        if(dto.getScene_location_long() != null) {
            scene.setScene_location_long(dto.getScene_location_long());
        }

        if(dto.getImage_id() != null) {
            if (!resourcesRepository.findById(dto.getImage_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid image id."));
            }
            scene.setImage_id(resourcesRepository.findById(dto.getImage_id()).get());
            Resources resource = resourcesRepository.findById(dto.getImage_id()).get();
            resource.setScenes1(scene);
            resourcesRepository.save(resource);
        }

        if(dto.getStory_image_id() != null) {
            if (!resourcesRepository.findById(dto.getStory_image_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid image id."));
            }
            scene.setStory_image_id(resourcesRepository.findById(dto.getStory_image_id()).get());
            Resources resource = resourcesRepository.findById(dto.getStory_image_id()).get();
            resource.setScenes2(scene);
            resourcesRepository.save(resource);
        }

        scenesRepository.save(scene);
        return ResponseEntity.ok(new MessageResponse("Your scene is successfully updated."));

    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/scenes")
    @Transactional
    public ResponseEntity<?> deleteGameScenes(@PathVariable UUID game_id, Authentication authentication) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games games = gamesRepository.findById(game_id).get();
        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(games)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete someone else's game scene!");
        }

        //System.out.println(games.getScenes());

        //remove all scenes
        Set<Scenes> sceneSet = games.getScenes();

        for(Scenes scene : sceneSet) {
            scenesRepository.delete(scene);
        }

        games.setScenes(null);

        gamesRepository.save(games);
        //System.out.println(games.getScenes());

        return ResponseEntity.ok(new MessageResponse("All games have been deleted."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/scenes/{scene_id}")
    @Transactional
    public Scenes printScene(@PathVariable UUID game_id, @PathVariable UUID scene_id, Authentication authentication) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Invalid game id.");
            }

            if (!scenesRepository.findById(scene_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Invalid scene id.");
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Scenes scene = scenesRepository.findById(scene_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: You can't view someone else's scene.");
            }

            if (hasAuthority && !game.getScenes().contains(scene)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene not found.");
            }

            return scene;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/scenes/{scene_id}")
    @Transactional
    public ResponseEntity<?> deleteScene(@PathVariable UUID game_id, @PathVariable UUID scene_id, Authentication authentication) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!scenesRepository.findById(scene_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid scene id."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Scenes scene = scenesRepository.findById(scene_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete someone else's scene."));
            }

            if (hasAuthority && !game.getScenes().contains(scene)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Scene not found."));
            }


            //remove scene from game
            Set<Scenes> sceneSet = game.getScenes();
            Set<Scenes> helperSet = new HashSet<>(); //cant modify current iterating list

            for(Scenes x : sceneSet) {
                if(!x.getId().equals(scene_id)) {
                    helperSet.add(x);
                }
            }

            game.setScenes(helperSet);
            gamesRepository.save(game);

            scenesRepository.deleteById(scene_id);
            return ResponseEntity.ok(new MessageResponse("Scene has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/scenes/{scene_id}/scenepoint")
    @Transactional
    public ScenePoints getScenePoints(@PathVariable UUID game_id, Authentication authentication, @PathVariable UUID scene_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Game does not exist.");
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene does not exist.");
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't view someone else's scene points!");
        }

        if (!game.getScenes().contains(scene)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene not found.");
        }

        return scene.getScene_points_id();
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games/{game_id}/scenes/{scene_id}/scenepoint")
    @Transactional
    public ResponseEntity<?> postScenePoint(@PathVariable UUID game_id, Authentication authentication, @RequestBody ScenePointsDTO dto, @PathVariable UUID scene_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post points in someone else's game scene!");
        }

        if (!game.getScenes().contains(scene)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene not found."));
        }

        ScenePoints points = new ScenePoints();

        points.setScenes(scene);
        scene.setScene_points_id(points);

        points.setMaximum_points(dto.getMaximum_points());
        points.setMinimum_points(dto.getMinimum_points());
        points.setPenalty_points(dto.getPenalty_points());
        points.setRemove_interval(dto.getRemove_interval());
        points.setRemove_on_wrong_answer(dto.getRemove_on_wrong_answer());
        points.setTime_based(dto.getTime_based());
        points.setRemove_on_hint(dto.getRemove_on_hint());

        scenepointsRepository.save(points);
        return ResponseEntity.ok(new MessageResponse("Points have been added successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/scenes/{scene_id}/scenepoints/{scenepoints_id}")
    @Transactional
    public ResponseEntity<?> deleteScenePoints(@PathVariable UUID game_id, @PathVariable UUID scene_id, Authentication authentication, @PathVariable UUID scenepoints_id) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!scenesRepository.findById(scene_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid scene id."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Scenes scene = scenesRepository.findById(scene_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete someone else's scene."));
            }

            if (!game.getScenes().contains(scene)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Scene not found."));
            }

            if (!scene.getScene_points_id().getId().equals(scenepoints_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Points not found."));
            }

            scene.setScene_points_id(null);
            scenepointsRepository.deleteById(scenepoints_id);
            return ResponseEntity.ok(new MessageResponse("Points has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/scenes/{scene_id}/question")
    @Transactional
    public Questions getQuestion(@PathVariable UUID game_id, Authentication authentication, @PathVariable UUID scene_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Game does not exist.");
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene does not exist.");
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't view someone else's game!");
        }

        if (!game.getScenes().contains(scene)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene not found.");
        }

        return scene.getQuestion_id();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games/{game_id}/scenes/{scene_id}/question")
    @Transactional
    public ResponseEntity<?> postQuestion(@PathVariable UUID game_id, Authentication authentication, @RequestBody QuestionsDTO dto, @PathVariable UUID scene_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post a question in someone else's game scene!");
        }

        if (!game.getScenes().contains(scene)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene not found."));
        }

        Questions question = new Questions();

        question.setScenes(scene);
        scene.setQuestion_id(question);

        question.setAccepts(dto.getAccepts());
        question.setMessage(dto.getMessage());
        question.setCase_sensitive_text_answers(dto.getCase_sensitive_text_answers());


        questionsRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Question has been created successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/scenes/{scene_id}/{question_id}")
    @Transactional
    public ResponseEntity<?> deleteQuestion(@PathVariable UUID game_id, @PathVariable UUID scene_id, Authentication authentication, @PathVariable UUID question_id) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!scenesRepository.findById(scene_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid scene id."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Scenes scene = scenesRepository.findById(scene_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete someone else's scene."));
            }

            if (!game.getScenes().contains(scene)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Scene not found."));
            }

            if (!scene.getQuestion_id().getId().equals(question_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Question not found."));
            }

            scene.setQuestion_id(null);
            questionsRepository.deleteById(question_id);
            return ResponseEntity.ok(new MessageResponse("Scene has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/scenes/{scene_id}/question/{question_id}/answers")
    @Transactional
    public Set<Answers> getAnswers(@PathVariable UUID game_id, Authentication authentication, @PathVariable UUID scene_id, @PathVariable UUID question_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Game does not exist.");
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene does not exist.");
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();
        Questions question = questionsRepository.findById(question_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't view someone else's game!");
        }

        if (!game.getScenes().contains(scene)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Scene not found.");
        }

        if (!scene.getQuestion_id().getId().equals(question_id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Question not found.");
        }

        return question.getAnswers();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games/{game_id}/scenes/{scene_id}/question/{question_id}/answers")
    @Transactional
    public ResponseEntity<?> postAnswer(@PathVariable UUID game_id, Authentication authentication, @RequestBody AnswersDTO dto, @PathVariable UUID scene_id, @PathVariable UUID question_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        if (!scenesRepository.findById(scene_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene does not exist."));
        }

        if (!questionsRepository.findById(question_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Question does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(scene_id).get();
        Questions question = questionsRepository.findById(question_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't post a question in someone else's game scene!");
        }

        if (!game.getScenes().contains(scene)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene not found."));
        }

        if (!scene.getQuestion_id().getId().equals(question_id)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Question not found."));
        }
        System.out.println(question.getAnswers());
        Answers answer = new Answers();

        answer.setQuestions(question);

        Set<Answers> questionSet = question.getAnswers();
        questionSet.add(answer);
        question.setAnswers(questionSet);
        questionsRepository.save(question);

        answer.setAnswer_value(dto.getAnswer_value());
        answer.setNumber_value(dto.getNumber_value());
        answer.setDate_value(dto.getDate_value());
        answer.setAnswer_options(dto.getAnswer_options());


        answersRepository.save(answer);
        System.out.println(question.getAnswers());
        return ResponseEntity.ok(new MessageResponse("Answer has been created successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/scenes/{scene_id}/question/{question_id}/answers")
    @Transactional
    public ResponseEntity<?> deleteAnswers(@PathVariable UUID game_id, @PathVariable UUID scene_id, Authentication authentication, @PathVariable UUID question_id) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!scenesRepository.findById(scene_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid scene id."));
            }

            if (!questionsRepository.findById(question_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Question does not exist."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Scenes scene = scenesRepository.findById(scene_id).get();
            Questions question = questionsRepository.findById(question_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete someone else's scene."));
            }

            if (!game.getScenes().contains(scene)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Scene not found."));
            }

            if (!scene.getQuestion_id().getId().equals(question_id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Question not found."));
            }
            System.out.println(question.getAnswers());
            Set<Answers> questionSet = question.getAnswers();

            for(Answers answer : questionSet) {
                answersRepository.delete(answer);
            }

            question.setAnswers(null);

            questionsRepository.save(question);
            System.out.println(question.getAnswers());
            return ResponseEntity.ok(new MessageResponse("Answers have been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/paths")
    @Transactional
    public Set<Paths> printGamePaths(@PathVariable UUID game_id, Authentication authentication) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game does not exist.");
        }
        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't view someone else's path!");
        }

        return game.getPaths();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PostMapping("/games/{game_id}/paths")
    @Transactional
    public ResponseEntity<?> postGamePath(@PathVariable UUID game_id, Authentication authentication,  @RequestBody PathsDTO dto) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        if (!scenesRepository.findById(dto.getScene_id()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Scene given does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();
        Scenes scene = scenesRepository.findById(dto.getScene_id()).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You can't create a path in someone else's game."));
        }

        Paths path = new Paths();

        path.setName(dto.getName());

        Set<Paths> sceneSet = scene.getPaths();
        sceneSet.add(path);
        scene.setPaths(sceneSet);

        Set<Paths> gameSet = game.getPaths();
        gameSet.add(path);
        game.setPaths(gameSet);

        path.setGames(game);

        gamesRepository.save(game);
        scenesRepository.save(scene);

        return ResponseEntity.ok(new MessageResponse("Path has been created successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/paths")
    @Transactional
    public ResponseEntity<?> deleteGamePaths(@PathVariable UUID game_id, Authentication authentication) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Game does not exist."));
        }

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();


        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        if (hasAuthority && !user.getGames().contains(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't delete someone else's game path!");
        }

        Set<Paths> pathSet = game.getPaths();
        Set<Scenes> sceneSet = game.getScenes();

        System.out.println(game.getPaths());
        for (Scenes scene : sceneSet) {
            scene.setPaths(null);
            scenesRepository.save(scene);
        }

        for (Paths path : pathSet) {
            path.setScenes(null);
        }

        game.setScenes(sceneSet);
        game.setPaths(null);
        gamesRepository.save(game);

        for(Paths path : pathSet) {
                path.setGames(null);
                pathsRepository.delete(path);
        }

        return ResponseEntity.ok(new MessageResponse("Paths have been deleted."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @GetMapping("/games/{game_id}/paths/{path_id}")
    @Transactional
    public PathsProjection printPath(@PathVariable UUID game_id, Authentication authentication, @PathVariable UUID path_id) {

        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Invalid game id.");
            }

            if (!pathsRepository.findPathsById(path_id).isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Invalid path id.");
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Paths path = pathsRepository.findPathsById(path_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            // check if path exists in any of the user's games
            Set<Games> gameSet = user.getGames();

            boolean flag = false;
            for (Games x: gameSet) {
                if (!x.getPaths().contains(path)) {
                    flag = true;
                } else {
                    flag = false;
                }
            }

            if (hasAuthority && flag) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: You can't view someone else's game path.");
            }

            if (hasAuthority && !game.getPaths().contains(path)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Path not found.");
            }

            return pathsRepository.findById(path.getId());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @DeleteMapping("/games/{game_id}/paths/{path_id}")
    @Transactional
    public ResponseEntity<?> deletePath(@PathVariable UUID game_id, @PathVariable UUID path_id, Authentication authentication) {
        try {

            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!pathsRepository.findPathsById(path_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid path id."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            Paths path = pathsRepository.findPathsById(path_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

            if (hasAuthority && !user.getGames().contains(game)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You can't delete a path in someone else's game."));
            }

            if (hasAuthority && !game.getPaths().contains(path)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Path not found."));
            }

            Set<Scenes> sceneSet = game.getScenes();

            for (Scenes scene : sceneSet) {
                if (scene.getPaths().contains(path)) {
                    scene.getPaths().remove(path);
                }
            }

            game.getPaths().remove(path);
            pathsRepository.deleteById(path_id);

            return ResponseEntity.ok(new MessageResponse("Path has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR')")
    @PatchMapping("/games/{game_id}/paths/{path_id}")
    @Transactional
    public ResponseEntity<?> editGamePath(@RequestBody PathsDTO dto, @PathVariable UUID game_id, Authentication authentication, @PathVariable UUID path_id) {

        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid game id."));
        }

        if (!pathsRepository.findPathsById(path_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid path id."));
        }

        Games game = gamesRepository.findById(game_id).get();
        User user = userRepository.findByUsername(authentication.getName()).get();
        Paths path = pathsRepository.findPathsById(path_id).get();

        boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CREATOR"));

        Set<Games> gameSet = user.getGames();

        boolean flag = false;
        for (Games x: gameSet) {
            if (!x.getPaths().contains(path)) {
                flag = true;
            } else {
                flag = false;
            }
        }

        if (hasAuthority && flag) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You can't edit a path in someone else's game!"));
        }

        if (hasAuthority && !game.getPaths().contains(path)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Path not found."));
        }
        
        if(dto.getFinish_message() != null) {
            path.setFinish_message(dto.getFinish_message());
        }

        if(dto.getName() != null) {
            path.setName(dto.getName());
        }

        if(dto.getStory_back_cover_colour() != null) {
            path.setStory_back_cover_colour(dto.getStory_back_cover_colour());
        }

        if(dto.getStory_back_cover_text_colour() != null) {
            path.setStory_back_cover_text_colour(dto.getStory_back_cover_text_colour());
        }

        if(dto.getStory_finish_message() != null) {
            path.setStory_finish_message(dto.getStory_finish_message());
        }

        if(dto.getImage_id() != null) {
            if (!resourcesRepository.findById(dto.getImage_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid image id."));
            }
            path.setImage_id(resourcesRepository.findById(dto.getImage_id()).get());
            Resources resource = resourcesRepository.findById(dto.getImage_id()).get();
            resource.setPaths(path);
            resourcesRepository.save(resource);
        }

        if(dto.getStory_image_id() != null) {
            if (!resourcesRepository.findById(dto.getStory_image_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid image id."));
            }
            path.setStory_image_id(resourcesRepository.findById(dto.getStory_image_id()).get());
            Resources resource = resourcesRepository.findById(dto.getStory_image_id()).get();
            resource.setPaths2(path);
            resourcesRepository.save(resource);
        }

        pathsRepository.save(path);
        return ResponseEntity.ok(new MessageResponse("Your path is successfully updated."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @PostMapping("/games/{game_id}/registration")
    @Transactional
    public ResponseEntity<?> registerToGame(Authentication authentication, @PathVariable UUID game_id) {

        User user = userRepository.findByUsername(authentication.getName()).get();
        Games game = gamesRepository.findById(game_id).get();

        if (!gamesRepository.findById(game_id).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid game id."));
        }

        String registration_code = UUID.randomUUID().toString().replace("-","").substring(0,6);

        GameRegistrations registration = new GameRegistrations(registration_code);

        Set<GameRegistrations> gameSet = game.getRegistrations();
        gameSet.add(registration);
        game.setRegistrations(gameSet);

        Set<GameRegistrations> userSet = user.getRegistrations();
        userSet.add(registration);
        user.setRegistrations(userSet);

        registration.setGame(game);
        registration.setUser(user);

        gamesRepository.save(game);
        userRepository.save(user);
        gameRegistrationsRepository.save(registration);
        System.out.println("games" + game.getRegistrations());
        System.out.println("user" + user.getRegistrations());
        return ResponseEntity.ok(new MessageResponse("Registered successfully, registration code: " + registration_code));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('USER')")
    @DeleteMapping("/games/{game_id}/registrations/{registration_id}")
    @Transactional
    public ResponseEntity<?> deleteRegistration(@PathVariable UUID game_id, Authentication authentication, @PathVariable UUID registration_id) {
        try {
            if (!gamesRepository.findById(game_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid game id."));
            }

            if (!gameRegistrationsRepository.findById(registration_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid registration id."));
            }

            Games game = gamesRepository.findById(game_id).get();
            User user = userRepository.findByUsername(authentication.getName()).get();
            GameRegistrations registration = gameRegistrationsRepository.findById(registration_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if (!hasAuthority && !user.getRegistrations().contains(registration)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: You aren't registered in this game."));
            }

            if (!game.getRegistrations().contains(registration)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Registration not found."));
            }

            System.out.println("user" + user.getRegistrations());
            System.out.println("game" + game.getRegistrations());
            //remove registration from game
            Set<GameRegistrations> gameSet = game.getRegistrations();
            Set<GameRegistrations> helperSet = new HashSet<>(); //cant modify current iterating list

            for(GameRegistrations x : gameSet) {
                if(!x.getId().equals(registration_id)) {
                    helperSet.add(x);
                }
            }

            game.setRegistrations(helperSet);
            gamesRepository.save(game);

            //remove registration from user
            Set<GameRegistrations> userSet = user.getRegistrations();
            Set<GameRegistrations> helperSet2 = new HashSet<>(); //cant modify current iterating list

            for(GameRegistrations x : userSet) {
                if(!x.getId().equals(registration_id)) {
                    helperSet2.add(x);
                }
            }

            user.setRegistrations(helperSet2);
            userRepository.save(user);

            gameRegistrationsRepository.deleteById(registration_id);
            System.out.println("user" + user.getRegistrations());
            System.out.println("game" + game.getRegistrations());
            return ResponseEntity.ok(new MessageResponse("Registration has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @GetMapping("/play/games")
    @Transactional
    public List<Games> getAvailableGames(Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName()).get();
        List<Games> gameList = new ArrayList<>();


        for (Games game : gamesRepository.findAll()) {
            if ((game.getEnds().isAfter(LocalDateTime.now()) && game.getStarts().isBefore(LocalDateTime.now())) && (game.getRegistrations_ends().isAfter(LocalDateTime.now()) && game.getRegistrations_start().isBefore(LocalDateTime.now())) && (gameRegistrationsRepository.findAllByGameId(game.getId()).size() < game.getParticipants_limit())) {

                for (GameRegistrations registration : gameRegistrationsRepository.findAllByGameId(game.getId())) { //check if registrations are open and user is not registered
                    if (game.getRegistrations_ends().isAfter(LocalDateTime.now()) && !registration.getUser().getId().equals(user.getId())) {
                        gameList.add(game);
                        break;
                    }
                }

            }
        }
        return gameList;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @GetMapping("/play/{game_id}")
    @Transactional
    public ResponseEntity<?> playGame(@PathVariable UUID game_id, Authentication authentication) {
        try {
            if(!gamesRepository.existsById(game_id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game does not exist.");
            }

            User user = userRepository.findByUsername(authentication.getName()).get();
            Games game = gamesRepository.findById(game_id).get();

            boolean hasAuthority = false;

            if ((!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
                hasAuthority = true;
            }

            if (hasAuthority && !user.getGames().contains(game)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: You can't view someone else's game");
            }

            boolean isRegistered = false;

            for (GameRegistrations registration : gameRegistrationsRepository.findAllByGameId(game_id)) {
                if (registration.getUser().getId().equals(user.getId())) {
                    isRegistered = true;
                    break;
                }
            }

            return ResponseEntity.ok((new GameResponse(isRegistered,
                    game.getTitle(),
                    game.getCreate_story(),
                    game.getDescription(),
                    game.getEnds(),
                    game.getIs_public(),
                    game.getMax_skips(),
                    game.getGame_mode(),
                    game.getParticipants_limit(),
                    game.getRegistrations_ends(),
                    game.getRegistrations_start(),
                    game.getStarts(),
                    game.getStory_cover_colour(),
                    game.getStory_cover_text_colour(),
                    game.getStory_title(),
                    game.getTerms(),
                    game.getPaths(),
                    game.getImage_id(),
                    game.getUser().getId(),
                    game.getStory_image_id())));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @GetMapping("/moves")
    @Transactional
    public List<Moves> printMoves() {
        return movesRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @GetMapping("/play/leaderboard/{game_id}")
    @Transactional
    public List<Moves> printLeaderBoard(@PathVariable UUID game_id) {
        if (!gamesRepository.findById(game_id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found.");
        }

        List<Moves> sortedList =  new ArrayList<>();

        int max = 0;
        int min = 1000000;
        Moves topMove = null;

        for (User user : userRepository.findAll()) {
            for (Moves move : movesRepository.findAllByUserId(user.getId())) {

                if (move.getPoints() != null) {
                    if (move.getPoints() > max) {
                        max = move.getPoints();
                        topMove = move;
                    }
                } else {
                    if (move.getIs_valid_move()) {
                        Scenes scene = scenesRepository.findById(move.getScene_id()).get();
                        if (scenepointsRepository.findById(scene.getScene_points_id().getId()).get().getMaximum_points() > max) {
                            topMove = move;
                            max = scenepointsRepository.findById(scene.getScene_points_id().getId()).get().getMaximum_points();
                        }
                    } else {
                        Scenes scene = scenesRepository.findById(move.getScene_id()).get();
                        if (scenepointsRepository.findById(scene.getScene_points_id().getId()).get().getMinimum_points() < min) {
                            topMove = move;
                            min = scenepointsRepository.findById(scene.getScene_points_id().getId()).get().getMinimum_points();
                        }
                    }
                }
            }
            max = 0;
            min = 1000000;
            sortedList.add(topMove);
        }

        return sortedList;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('PLAYER')")
    @PostMapping("/moves")
    @Transactional
    public ResponseEntity<?> postMove(Authentication authentication, @RequestBody MovesDTO dto) {

        User user = userRepository.findByUsername(authentication.getName()).get();

        Moves move = new Moves();

        move.setCreated(LocalDateTime.now());

        if(dto.getResource_id() != null) {
            if (!resourcesRepository.findById(dto.getResource_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid resource id."));
            }
            Resources resource = resourcesRepository.findById(dto.getResource_id()).get();
            Set<Resources> resourceSet = move.getResources();
            resourceSet.add(resource);
            move.setResources(resourceSet);
        }

        Set<Moves> moveSet = user.getMoves();
        moveSet.add(move);
        user.setMoves(moveSet);
        move.setUser(user);

        userRepository.save(user);

        if(dto.getId_map() != null) {
            move.setId_map(dto.getId_map());
        }

        if(dto.getIs_valid_move() != null) {
            move.setIs_valid_move(dto.getIs_valid_move());
        }

        /*if(dto.getAnnotation_answer_id() != null) {
            if (!annotationAnswersRepository.findById(dto.getAnnotation_answer_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid annotation answer id!"));
            }
            move.setAnnotation_answer_id(dto.getAnnotation_answer_id());
        }*/

        if(dto.getGame_id() != null) {
            if (!gamesRepository.findById(dto.getGame_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid game id!"));
            }
            move.setGame_id(dto.getGame_id());
        }

        if(dto.getScene_id() != null) {

            if(!gamesRepository.findById(dto.getGame_id()).get().getScenes().contains(scenesRepository.findById(dto.getScene_id()).get())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Scene can't be found in the game given!"));
            }

            if (!scenesRepository.findById(dto.getScene_id()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid scene id!"));
            }
            move.setScene_id(dto.getScene_id());
        }

        if(dto.getAnswers() != null) {
            move.setAnswers(dto.getAnswers());
        }

        if(dto.getLocation_lat() != null) {
            move.setLocation_lat(dto.getLocation_lat());
        }

        if(dto.getLocation_long() != null) {
            move.setLocation_long(dto.getLocation_long());
        }

        if(dto.getPoints() != null) {
            move.setPoints(dto.getPoints());
        }

        movesRepository.save(move);
        return ResponseEntity.ok(new MessageResponse("Move has been created successfully."));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CREATOR') or hasRole('USER')")
    @DeleteMapping("/moves/{move_id}")
    @Transactional
    public ResponseEntity<?> deleteScene(@PathVariable UUID move_id, Authentication authentication) {
        try {

            if (!movesRepository.findById(move_id).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid move id."));
            }


            User user = userRepository.findByUsername(authentication.getName()).get();
            Moves move = movesRepository.findById(move_id).get();

            boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if (!hasAuthority && !user.getMoves().contains(move)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("You can't delete some else's move!"));
            }

            System.out.println(user.getMoves());
            //remove move from user
            Set<Moves> moveSet = user.getMoves();
            Set<Moves> helperSet = new HashSet<>(); //cant modify current iterating list

            for(Moves x : moveSet) {
                if(!x.getId().equals(move_id)) {
                    helperSet.add(x);
                }
            }

            user.setMoves(helperSet);
            userRepository.save(user);

            movesRepository.deleteById(move_id);
            System.out.println(user.getMoves());
            return ResponseEntity.ok(new MessageResponse("Move has been deleted successfully."));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
        }
    }

}