package com.maciek.warcraftstatstracker.controller;

import com.maciek.warcraftstatstracker.model.Character;
import com.maciek.warcraftstatstracker.service.BlizzardApiService;
import com.maciek.warcraftstatstracker.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api")
public class CharacterController {

    private BlizzardApiService blizzardApiService;
    private CharacterService characterService;

    @Autowired
    public CharacterController(BlizzardApiService blizzardApiService, CharacterService characterService) {
        this.blizzardApiService = blizzardApiService;
        this.characterService = characterService;
    }

    @GetMapping("/character/{name}")
    public ResponseEntity<Character> getCharacter(@PathVariable String name, @RequestParam String realm, OAuth2Authentication oAuth2Authentication) {
        String blizzardApiResponse;
        try {
            blizzardApiResponse = blizzardApiService.getBlizzardCharacterData(name, realm, oAuth2Authentication);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.notFound().build();
        }

        Character character = characterService.getCharacterFromApi(blizzardApiResponse);
        return ResponseEntity.ok(character);
    }
}
