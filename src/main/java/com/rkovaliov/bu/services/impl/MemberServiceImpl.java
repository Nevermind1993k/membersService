package com.rkovaliov.bu.services.impl;

import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import com.rkovaliov.bu.repositories.MemberRepository;
import com.rkovaliov.bu.services.MemberService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MemberServiceImpl implements MemberService {

    private static final AtomicInteger counter = new AtomicInteger();

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
    public void save(Member member) {
        memberRepository.save(new Member(nextValue(), member.getFirstName(), member.getLastName(), member.getDateOfBirth(), member.getPostalCode(), null));
    }

    @Override
    @Transactional
    public void deleteById(long id) throws MemberNotExistsException {
        Optional<Member> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            memberRepository.deleteById(id);
            //todo delete image from hard disc
        } else {
            throw new MemberNotExistsException(id);
        }
    }

    @Override
    @Transactional
    public void updateById(long id, Member updatedMember) throws MemberNotExistsException {
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

    private static int nextValue() {
        int toReturn = counter.getAndIncrement();
        if (toReturn == 0) {
            toReturn = counter.getAndIncrement();
        }
        return toReturn;
    }

}