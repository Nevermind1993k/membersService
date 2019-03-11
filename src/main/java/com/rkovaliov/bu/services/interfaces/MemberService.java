package com.rkovaliov.bu.services.interfaces;

import com.rkovaliov.bu.controllers.MemberController;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;

import java.util.List;

public interface MemberService {

    List<Member> getAll();

    Member getById(long id) throws MemberNotExistsException;

    long save(MemberController.MemberToSave member);

    void deleteById(long id) throws MemberNotExistsException;

    void updateById(long id, MemberController.MemberToSave updatedMember) throws MemberNotExistsException;

    void saveImage(long id, byte[] file) throws MemberNotExistsException;

    void deleteImageById(long id) throws MemberNotExistsException;
}
