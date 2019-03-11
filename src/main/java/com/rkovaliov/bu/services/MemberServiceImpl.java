package com.rkovaliov.bu.services;

import com.rkovaliov.bu.controllers.MemberController;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.repositories.MemberRepository;
import com.rkovaliov.bu.services.interfaces.MemberService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SequenceGeneratorService sequenceGenerator;

    public MemberServiceImpl(MemberRepository memberRepository, SequenceGeneratorService sequenceGenerator) {
        this.memberRepository = memberRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member getById(long id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            throw new MemberNotExistsException(id);
        }
    }

    @Override
    @Transactional
    public long save(MemberController.MemberToSave member) {
        Member toSave = new Member(sequenceGenerator.generateSequence(Member.SEQUENCE_NAME), member.getFirstName(), member.getLastName(), member.getDateOfBirth(), member.getPostalCode(), null);
        memberRepository.save(toSave);
        return toSave.getId();
    }

    @Override
    @Transactional
    public void deleteById(long id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            memberRepository.deleteById(id);
        } else {
            throw new MemberNotExistsException(id);
        }
    }

    @Override
    @Transactional
    public void updateById(long id, MemberController.MemberToSave updatedMember) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            memberById.setFirstName(updatedMember.getFirstName());
            memberById.setLastName(updatedMember.getLastName());
            memberById.setDateOfBirth(updatedMember.getDateOfBirth());
            memberById.setPostalCode(updatedMember.getPostalCode());
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id);
        }
    }

    @Override
    @Transactional
    public void saveImage(long id, byte[] file) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            if (memberById.getImage() != null) {
                memberById.setImage(null);
            }
            memberById.setImage(new Binary(BsonBinarySubType.BINARY, file));
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id);
        }
    }

    @Override
    public void deleteImageById(long id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            Member memberById = byId.get();
            memberById.setImage(null);
            memberRepository.save(memberById);
        } else {
            throw new MemberNotExistsException(id);
        }
    }
}
