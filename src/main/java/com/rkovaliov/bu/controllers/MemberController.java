package com.rkovaliov.bu.controllers;

import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping(value = CREATE_MEMBER_URL)
    public ResponseEntity<Object> createMember(@Valid @RequestBody Member member) {
        memberService.save(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = READ_MEMBER_URL)
    public ResponseEntity<Object> getMemberById(@RequestParam("id") long id) throws MemberNotExistsException {
        return new ResponseEntity<>(memberService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = LIST_MEMBERS_URL)
    public ResponseEntity<Object> readAllMembers() {
        return new ResponseEntity<>(memberService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = UPDATE_MEMBER_URL)
    public ResponseEntity<Object> updateMemberById(@RequestParam("id") long id, @RequestBody Member updatedMember) throws MemberNotExistsException {
        memberService.updateById(id, updatedMember);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = DELETE_MEMBER_URL)
    public ResponseEntity<Object> deleteMemberById(@RequestParam("id") long id) throws MemberNotExistsException {
        memberService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = SAVE_IMAGE_URL)
    public ResponseEntity<Object> saveImageToMember(@RequestParam("id") long id, @RequestBody byte[] file) throws MemberNotExistsException {
        if (file == null || file.length == 0 || file.length > 5000000) {
            return new ResponseEntity<>("Upload File Is Empty or too big", HttpStatus.BAD_REQUEST);
        }
        memberService.saveImage(id, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Test Controller to check if image save properly
    @GetMapping(value = "getImage",produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Object> getImage() throws MemberNotExistsException {
        Member byId = memberService.getById(1);
        byte[] imageInBytes = byId.getImage().getData();
        return new ResponseEntity<>(imageInBytes, HttpStatus.OK);
    }
}
