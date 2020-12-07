package gov.usaf;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO: 1. Probably want to be able to return from leave and close out
//TODO: 2. Probably want a way to adjust leave dates
//TODO: 3. Will probably cost the govt tons of extra cash, though, so withhold it for future contract

//TODO: Add a user interface. Probably want to feed this backend out on an API and manage that with a pretty frontend, see 3 above

public class LeaveTracker {
    ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();

    public static int getYearlyLeave(User user) {
        return 30;
    }

    public int getRemainingLeave(User user) {
        int daysUsed = leaveRequests.stream()
                .filter(r->
                        (r.getStatus().getValue0() ==  LeaveRequest.Status.CLOSED ||
                         r.getStatus().getValue0() == LeaveRequest.Status.APPROVED ) &&
                        r.getRequester()==user.getId() &&
                        r.endDate().getYear() >= LocalDate.now().getYear())
                .mapToInt(request -> {
                    if (request.startDate().getYear() == LocalDate.now().getYear()) {
                        return (int) (request.startDate().until(request.endDate(), ChronoUnit.DAYS) + 1);
                    }else if (request.startDate().getYear() < LocalDate.now().getYear()) {
                        return (int) (LocalDate.of(LocalDate.now().getYear(),1,1).until(request.endDate(), ChronoUnit.DAYS) + 1);
                    }
                    return 0;
                })
                .sum();
        return getYearlyLeave(user)-daysUsed;
    }

    public List<Triplet<Integer, Integer, Integer>> getSubordinateLeaveRequests(User supervisor, LeaveRequest.Status status) {
        ArrayList<Triplet<Integer, Integer, Integer>> list = new ArrayList<>();
        supervisor.getSubordinates().forEach(user->{
            int remainingLeave = getRemainingLeave(user);
            int userId = user.getId();
            list.addAll(leaveRequests.stream().filter(request->request.getRequester()==user.getId() && request.getStatus().getValue0() == status).map(request->
               new Triplet<> (userId,remainingLeave, (int) (request.startDate().until(request.endDate(),ChronoUnit.DAYS) + 1))
            ).collect(Collectors.toList()));
        });

        return list;
    }

    public String requestLeave(User user, LeaveRequest request) {
        request.setRequester(user.getId());
        int days = (int)(request.from.until(request.to, ChronoUnit.DAYS)+1);
        if (getRemainingLeave(user) >= days) {
            leaveRequests.add(request);
            return "Leave has been submitted";
        }else{
            return "Insufficient leave balance";
        }
    }
    public String requestLeave(User user, LocalDate from, LocalDate to) {
        LeaveRequest request = new LeaveRequest(from,to);
        return requestLeave(user,request);
    }

    public List<LeaveRequest> getOpenLeaveRequests(User user) {
        return getLeaveRequests(user,LeaveRequest.Status.OPEN);
    }
    public List<LeaveRequest> getLeaveRequests(User user, LeaveRequest.Status status) {
        return leaveRequests.stream().filter(request-> request.getRequester() == user.getId() && (status== LeaveRequest.Status.ANY || request.getStatus().getValue0() == status)).collect(Collectors.toList());
    }
    public void approveLeave(User supervisor, int requestId) {
        List<LeaveRequest> requests = leaveRequests.stream().filter(request->request.getId() == requestId).collect(Collectors.toList());
        if (!requests.isEmpty()) {
            LeaveRequest request = requests.get(0);
            if (supervisor.getSubordinates().stream().anyMatch(user -> user.getId() == request.getRequester())) {
                request.updateStatus(LeaveRequest.Status.APPROVED);
            }
        }
    }

    public List<Pair<Integer, Integer>> getAllRemainingLeave(User supervisor) {
        return supervisor.getSubordinates().stream().map(user->new Pair<>(user.getId(),getRemainingLeave(user))).collect(Collectors.toList());
    }

    public void denyLeave(User supervisor, int requestId) {
        List<LeaveRequest> requests = leaveRequests.stream().filter(request->request.getId() == requestId).collect(Collectors.toList());
        if (!requests.isEmpty()) {
            LeaveRequest request = requests.get(0);
            if (supervisor.getSubordinates().stream().anyMatch(user -> user.getId() == request.getRequester())) {
                request.updateStatus(LeaveRequest.Status.DENIED);
            }
        }
    }
}
