package com.rkovaliov.bu.services.interfaces;

import com.rkovaliov.bu.MembersServiceApp;
import com.rkovaliov.bu.controllers.MemberController;
import com.rkovaliov.bu.entities.Member;
import com.rkovaliov.bu.exceptions.MemberNotExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MembersServiceApp.class)
public class MemberServiceTest {

    @Mock
    @Autowired
    private MemberService memberService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByIdTest() throws MemberNotExistsException {
        Member mockMember = getMockedMember();
        when(memberService.getById(anyLong())).thenReturn(mockMember);

        Member byId = memberService.getById(1);
        assertEquals(byId.getFirstName(), mockMember.getFirstName());
        verify(memberService, atLeastOnce()).getById(1);
    }

    private Member getMockedMember() {
        return new Member(1, "Vasia", "Pupkin", new Date(), "1234", null);
    }

    @Test(expected = MemberNotExistsException.class)
    public void getByIdWithWrongIdTest() throws MemberNotExistsException {
        long memberId = 25;
        when(memberService.getById(memberId)).thenThrow(new MemberNotExistsException(memberId));
        Member byId = memberService.getById(memberId);
    }

    @Test
    public void getAllTest() {
        List<Member> memberList = new ArrayList<>();
        memberList.add(new Member(1, "Vasia", "Pupkin", new Date(), "1234", null));
        memberList.add(new Member(2, "Petya", "Some", new Date(), "4321", null));
        memberList.add(new Member(3, "Grisha", "Lazy", new Date(), "3214", null));
        when(memberService.getAll()).thenReturn(memberList);

        List<Member> result = memberService.getAll();
        assertEquals(3, result.size());
    }

    @Test
    public void saveTest() {
        Member mockedMember = getMockedMember();
        MemberController.MemberToSave memberToSave = new MemberController.MemberToSave(mockedMember.getFirstName(), mockedMember.getLastName(), mockedMember.getDateOfBirth(), mockedMember.getPostalCode());
        when(memberService.save(memberToSave)).thenReturn(mockedMember.getId());

        long result = memberService.save(memberToSave);
        assertEquals(mockedMember.getId(), result);
    }

    @Test(expected = MemberNotExistsException.class)
    public void deleteByIdWithWrongIdTest() throws MemberNotExistsException {
        Member mockedMember = getMockedMember();
        doThrow(new MemberNotExistsException(mockedMember.getId())).when(memberService).deleteById(anyLong());
        memberService.deleteById(mockedMember.getId());
    }

    @Test(expected = MemberNotExistsException.class)
    public void updateByIdWithWrongIdTest() throws MemberNotExistsException {
        Member mockedMember = getMockedMember();
        doThrow(new MemberNotExistsException(mockedMember.getId())).when(memberService).updateById(anyLong(), any(Member.class));

        mockedMember.setFirstName("someName");
        memberService.updateById(mockedMember.getId(), mockedMember);
    }
}