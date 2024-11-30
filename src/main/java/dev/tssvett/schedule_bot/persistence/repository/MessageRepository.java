package dev.tssvett.schedule_bot.persistence.repository;

import static dev.tssvett.schedule_bot.persistence.model.tables.Message.MESSAGE;
import dev.tssvett.schedule_bot.persistence.model.tables.records.MessageRecord;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final DSLContext dslContext;

    public List<MessageRecord> findAll() {
        return dslContext
                .select()
                .from(MESSAGE)
                .fetchInto(MessageRecord.class);
    }

    public void saveAll(List<MessageRecord> messages) {
        dslContext.batchInsert(messages)
                .execute();
    }

    public void deleteAll() {
        dslContext.deleteFrom(MESSAGE)
                .execute();
    }
}
