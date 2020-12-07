package gov.usaf;


import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaveTrackerTest {
    User admin;
    User supervisor;
    User user;
    User user2;
    LeaveTracker leaveTracker;
    UserTracker userTracker;
    LeaveRequest OneDay;
    LeaveRequest FortyFiveDay;
    LeaveRequest ThirtyDay;
    @BeforeEach
    public void setup() {
        userTracker = new UserTracker();
        leaveTracker = new LeaveTracker();
        user = UserTracker.generateUser();
        user2 = UserTracker.generateUser();
        admin = UserTracker.generateUser().setPermissionLevel(1);
        supervisor = UserTracker.generateUser().addSubordinate(user);
        OneDay = new LeaveRequest(LocalDate.of(2020,1,1),LocalDate.of(2020,1,1));
        ThirtyDay = new LeaveRequest(LocalDate.of(2020,1,1),LocalDate.of(2020,1,30));
        FortyFiveDay=new LeaveRequest(LocalDate.of(2020,1,1),LocalDate.of(2020,2,14));
    }

    @Test
    public void ShouldReturnStartingLeave() {
        assertEquals(30,LeaveTracker.getYearlyLeave(user));
    }
    @Test
    public void CanApproveLeave() {
        leaveTracker.requestLeave(user,OneDay);
        leaveTracker.approveLeave(supervisor,leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals(new Pair<>(LeaveRequest.Status.APPROVED,""), OneDay.getStatus());
    }
    @Test
    public void ShouldReturnRemainingLeave() {
        leaveTracker.requestLeave(user, OneDay);
        leaveTracker.approveLeave(supervisor, leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals(29,leaveTracker.getRemainingLeave(user));
    }
    @Test
    public void ShouldProvideSuccessMessage() {

        assertEquals("Leave has been submitted",leaveTracker.requestLeave(user, OneDay));
    }
    @Test
    public void ShouldProvideNoLeaveFailureMessage() {
        leaveTracker.requestLeave(user,ThirtyDay);
        leaveTracker.approveLeave(supervisor, leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals("Insufficient leave balance",leaveTracker.requestLeave(user, OneDay));
    }
    @Test
    public void ShouldReturnAllUserRemainingLeave() {
        assertEquals(Arrays.asList(new Pair<>(user.getId(), 30)), leaveTracker.getAllRemainingLeave(supervisor));
        leaveTracker.requestLeave(user,LocalDate.of(2020,1,1),LocalDate.of(2020,1,7));
        leaveTracker.approveLeave(supervisor, leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals( Arrays.asList(new Pair<>(user.getId(), 23)), leaveTracker.getAllRemainingLeave(supervisor));

    }
    @Test
    public void ShouldReturnAllOpenLeaveRequests() {
        leaveTracker.requestLeave(user,OneDay);
        assertEquals(Arrays.asList(new Triplet<Integer,Integer,Integer>(user.getId(),30,1)), leaveTracker.getSubordinateLeaveRequests(supervisor,LeaveRequest.Status.OPEN));
    }
    @Test
    public void CanDenyLeave() {
        leaveTracker.requestLeave(user,OneDay);
        leaveTracker.denyLeave(supervisor,leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals(new Pair<>(LeaveRequest.Status.DENIED,""), OneDay.getStatus());
    }
    @Test
    public void ShouldReturnAllUserLeaveRequests() {
        leaveTracker.requestLeave(user,OneDay);
        leaveTracker.denyLeave(supervisor,leaveTracker.getOpenLeaveRequests(user).get(0).getId());
        assertEquals(OneDay, leaveTracker.getLeaveRequests(user,LeaveRequest.Status.ANY).get(0));
    }


}
