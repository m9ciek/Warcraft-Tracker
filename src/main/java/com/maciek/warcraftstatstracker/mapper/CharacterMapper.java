package com.maciek.warcraftstatstracker.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maciek.warcraftstatstracker.model.Character;
import com.maciek.warcraftstatstracker.model.CharacterDetails;

import java.sql.Timestamp;

public class CharacterMapper {

    public static Character mapJSONToCharacter(String dataJSON) {
        Character character = new Character();
        CharacterDetails details = new CharacterDetails();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node;
        try {
            node = objectMapper.readValue(dataJSON, JsonNode.class);
            populateCharacter(node, character);
            populateCharacterDetails(node, details);
        } catch (Exception e) {
            e.printStackTrace();
        }
        character.setCharacterDetails(details);
        return character;
    }

    private static void populateCharacter(JsonNode node, Character characterToPopulate) {
        JsonNode child = node.get("realm");
        JsonNode childField = child.get("name");
        characterToPopulate.setRealm(childField.asText());

        child = node.get("name");
        characterToPopulate.setName(child.asText());

        child = node.get("id");
        characterToPopulate.setId(child.asLong());

        child = node.get("_links");
        childField = child.get("self");
        child = childField.get("href");
        characterToPopulate.setUrl(child.asText());

        child = node.get("character_class");
        childField = child.get("name");
        characterToPopulate.setCharacterClass(childField.asText());
    }

    private static void populateCharacterDetails(JsonNode node, CharacterDetails characterDetailsToPopulate) {
        JsonNode child = node.get("race");
        JsonNode childField = child.get("name");
        characterDetailsToPopulate.setRace(childField.asText());

        child = node.get("faction");
        childField = child.get("name");
        characterDetailsToPopulate.setFaction(childField.asText());

        child = node.get("active_spec");
        childField = child.get("name");
        characterDetailsToPopulate.setActiveSpec(childField.asText());

        child = node.get("last_login_timestamp");
        characterDetailsToPopulate.setLastLogin(new Timestamp(child.asLong()).toLocalDateTime());

        child = node.get("level");
        characterDetailsToPopulate.setLevel(child.asInt());

        child = node.get("average_item_level");
        characterDetailsToPopulate.setAverageItemLevel(child.asInt());

        //In case of not guild field provided
        child = node.get("guild");
        if (child != null) {
            childField = child.get("name");
            characterDetailsToPopulate.setGuild(childField.asText());
        }
    }

}
