package dev.tssvett.schedule_bot.bot.appstart;

import dev.tssvett.schedule_bot.parsing.flow.ParserFlow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "scheduling.parser.on-start", havingValue = "true")
public class OnApplicationStart {
    private final ParserFlow parserFlow;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        parserFlow.startParsingFlow();
    }
}
