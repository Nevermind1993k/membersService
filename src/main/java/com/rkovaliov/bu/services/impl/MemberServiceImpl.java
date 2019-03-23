package com.rkovaliov.bu.services.impl;

import com.rkovaliov.bu.controllers.MemberController;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.repositories.MemberRepository;
import com.rkovaliov.bu.services.MemberService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member getById(ObjectId id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findBy_id(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new MemberNotExistsException(id.toHexString());
        }
    }

    @Override
    @Transactional
    public String save(MemberController.MemberToSave member) {
        Member toSave = new Member(member.getFirstName(), member.getLastName(), member.getDateOfBirth(), member.getPostalCode());
        memberRepository.save(toSave);
        return toSave.get_id();
    }

    @Override
    @Transactional
    public void deleteById(ObjectId id) {
        memberRepository.deleteById(id.toHexString());
    }

    @Override
    @Transactional
    public void updateById(ObjectId id, MemberController.MemberToSave updatedMember) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findBy_id(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            memberById.setFirstName(updatedMember.getFirstName());
            memberById.setLastName(updatedMember.getLastName());
            memberById.setDateOfBirth(updatedMember.getDateOfBirth());
            memberById.setPostalCode(updatedMember.getPostalCode());
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id.toHexString());
        }
    }

    @Override
    @Transactional
    public void saveImage(ObjectId id, byte[] file) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findBy_id(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            if (memberById.getImage() != null) {
                memberById.setImage(null);
            }
            memberById.setImage(new Binary(BsonBinarySubType.BINARY, file));
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id.toHexString());
        }
    }

    @Override
    public void deleteImageById(ObjectId id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findBy_id(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            memberById.setImage(null);
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id.toHexString());
        }
    }
}
