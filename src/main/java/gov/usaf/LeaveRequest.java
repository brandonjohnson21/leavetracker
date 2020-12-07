package gov.usaf;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.javatuples.Pair;

import java.time.LocalDate;

public class LeaveRequest {
    int id;
    Status status;
    LocalDate from;
    LocalDate to;
    int requester;
    String statusMessage;
    public int getRequester() {
        return requester;
    }

    public void setRequester(int requester) {
        this.requester = requester;
    }

    public LeaveRequest(LocalDate from, LocalDate to) {
        this.from=from;
        this.to=to;
        this.status = Status.OPEN;
    }


/*    public LeaveRequest(int id, LocalDate from, LocalDate to) {
        this(from,to);
        this.id=id;
    }*/
    public void setId(int id) {
        this.id=id;
    }

    public LocalDate startDate() {
        return this.from;
    }
    public LocalDate endDate() {
        return this.to;
    }

    public Pair<Status, String> getStatus() {
        return new Pair<>(this.status,this.statusMessage);
    }
    public LeaveRequest updateStatus(Status status, String message) {
        this.status = status;
        this.statusMessage=message;
        return this;
    }
    public LeaveRequest updateStatus(Status status) {
        return updateStatus(status,"");
    }

    public int getId() {
        return this.id;
    }

    public enum Status {
        ANY,
        OPEN,
        APPROVED,
        DENIED,
        CLOSED
    }

}
