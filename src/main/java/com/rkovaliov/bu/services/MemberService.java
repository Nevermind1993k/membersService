package com.rkovaliov.bu.services;

import com.rkovaliov.bu.controllers.MemberController;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import org.bson.types.ObjectId;

import java.util.List;

public interface MemberService {

    List<Member> getAll();

    Member getById(ObjectId id) throws MemberNotExistsException;

    String save(MemberController.MemberToSave member);

    void deleteById(ObjectId id);

    void updateById(ObjectId id, MemberController.MemberToSave updatedMember) throws MemberNotExistsException;

    void saveImage(ObjectId id, byte[] file) throws MemberNotExistsException;

    void deleteImageById(ObjectId id) throws MemberNotExistsException;
}
