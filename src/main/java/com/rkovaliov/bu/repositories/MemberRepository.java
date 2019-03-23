package com.rkovaliov.bu.repositories;

import com.rkovaliov.bu.entities.Member;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

    Optional<Member> findBy_id(ObjectId _id);

}
