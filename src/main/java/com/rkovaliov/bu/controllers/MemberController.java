package com.rkovaliov.bu.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.services.interfaces.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class MemberController {

    private static final String CREATE_MEMBER_URL = "/create";
    private static final String READ_MEMBER_URL = "/read";
    private static final String LIST_MEMBERS_URL = "/readAll";
    private static final String UPDATE_MEMBER_URL = "/update";
    private static final String DELETE_MEMBER_URL = "/delete";
    private static final String SAVE_IMAGE_URL = "/saveImage";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @ApiOperation(value = "Create new member", notes = "Creates new member and returns his Id")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "firstName is required field"),
            @ApiResponse(code = 200, message = "1")})
    @PostMapping(value = CREATE_MEMBER_URL)
    public ResponseEntity<Object> createMember(@Valid @RequestBody MemberToSave memberToSave, BindingResult br) {
        // Handle binding errors
        if (br.hasErrors()) {
            return new ResponseEntity<>(br.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(memberService.save(memberToSave), HttpStatus.OK);
    }


    @ApiOperation(value = "Get member by id", notes = "Allows to get all information about member")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with this id is not exists"),
            @ApiResponse(code = 200, message = "{'id':1,'firstName':'Vasia','lastName':'Sinichkin'','dateOfBirth':'09-09-1990','postalCode':'1234','image':{'type':0,'data':'/9j/4AAQ...'}}")})
    @GetMapping(value = READ_MEMBER_URL)
    public ResponseEntity<Object> getMemberById(@RequestParam("id") long id) throws MemberNotExistsException {
        return new ResponseEntity<>(memberService.getById(id), HttpStatus.OK);
    }


    @ApiOperation(value = "Get members", notes = "Allows to get all information about all members")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "[{'id':1,'firstName':'Vasia','lastName':'Pupkin'','dateOfBirth':'09-09-1990','postalCode':'1234','image':{'type':0,'data':'/9j/4AAQ...'}},...]")})
    @GetMapping(value = LIST_MEMBERS_URL)
    public ResponseEntity<Object> getAllMembers() {
        return new ResponseEntity<>(memberService.getAll(), HttpStatus.OK);
    }


    @ApiOperation(value = "Update member by id", notes = "Updates information about member")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with this id is not exists"),
            @ApiResponse(code = 200, message = "")})
    @PutMapping(value = UPDATE_MEMBER_URL)
    public ResponseEntity<Object> updateMemberById(@RequestParam("id") long id, @RequestBody Member updatedMember) throws MemberNotExistsException {
        memberService.updateById(id, updatedMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete member by id", notes = "Deletes member")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with this id is not exists"),
            @ApiResponse(code = 200, message = "")})
    @DeleteMapping(value = DELETE_MEMBER_URL)
    public ResponseEntity<Object> deleteMemberById(@RequestParam("id") long id) throws MemberNotExistsException {
        memberService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Save image to member by id", notes = "Allows save image to member")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with this id is not exists"),
            @ApiResponse(code = 400, message = "Upload File Is Empty or too big"),
            @ApiResponse(code = 200, message = "")})
    @PutMapping(value = SAVE_IMAGE_URL)
    public ResponseEntity<Object> saveImageToMember(@RequestParam("id") long id, @RequestBody byte[] file) throws MemberNotExistsException {
        if (file == null || file.length == 0 || file.length > 5000000) {
            return new ResponseEntity<>("Upload File Is Empty or too big", HttpStatus.BAD_REQUEST);
        }
        memberService.saveImage(id, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Test Controller to check if image save properly
    @ApiOperation(value = "(TestController) Get image for member by id", notes = "(TestController) Allows to get image as image for member just to check if it works")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User with this id is not exists"),
            @ApiResponse(code = 200, message = "%some pretty image as jpeg%")})
    @GetMapping(value = "getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getImage(@RequestParam("id") long id) throws MemberNotExistsException {
        Member byId = memberService.getById(id);
        byte[] imageInBytes = byId.getImage().getData();
        return new ResponseEntity<>(imageInBytes, HttpStatus.OK);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberToSave {
        @NotNull(message = "firstName is required field")
        private String firstName;

        @NotNull(message = "lastName is required field")
        private String lastName;

        @NotNull(message = "dateOfBirth is required field")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private Date dateOfBirth;

        @NotNull(message = "postalCode is required field")
        private String postalCode;

    }
}
