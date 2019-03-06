package com.rkovaliov.bu.services;

import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;

import java.util.List;

public interface MemberService {

    List<Member> getAll();

    Member getById(long id) throws MemberNotExistsException;

    void save(Member member);

    void deleteById(long id) throws MemberNotExistsException;

    void updateById(long id, Member updatedMember) throws MemberNotExistsException;

    void saveImage(long id, byte[] file) throws MemberNotExistsException;

}
