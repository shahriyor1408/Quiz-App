package uz.hibernate.service;

import uz.hibernate.dao.SolveTestDAO;
import uz.hibernate.domains.Answer;
import uz.hibernate.domains.Question;
import uz.hibernate.vo.AppErrorVO;
import uz.hibernate.vo.DataVO;
import uz.hibernate.vo.http.Response;
import uz.jl.BaseUtils;
import uz.jl.Colors;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SolveTestService {
    private static SolveTestService instance;

    private SolveTestService() {
    }

    public static Response<DataVO<Void>> solveTest(String subjectId, String quizType, String quizNumber) {
        Map<Long, Map<Question, Answer>> questionAnswerMap = SolveTestDAO.solveTest(subjectId, quizType, quizNumber);
        if (questionAnswerMap.isEmpty()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Not found questions for this subject")
                    .build()), false);
        }
        LocalDateTime endTime = LocalDateTime.now().plusSeconds(Integer.parseInt(quizNumber) * 30L);
        LocalDateTime startTime = LocalDateTime.now();
        int i = 1, counterOfCorrectAnswer = 0, answersNumber = 0;
        Random random = new Random();

        List<Integer> integers = new ArrayList<>();
        int randomness;
        String givenAnswer = "";

        BaseUtils.println("*****  Test is started, GOOD LUCK!!!  *****", Colors.PURPLE);

        while (LocalDateTime.now().isBefore(endTime) && i < Integer.parseInt(quizNumber)) {
            randomness = random.nextInt(questionAnswerMap.size());
            while (integers.contains(randomness)) {
                randomness = random.nextInt(questionAnswerMap.size());
            }
            integers.add(randomness);

            long totalSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime);
            int hours = (int) totalSeconds / 3600;
            int minutes = (int) totalSeconds % 3600 / 60;
            int seconds = (int) totalSeconds % 3600 % 60;

            System.out.println("(Time left: " + hours + "h" + ":" + minutes + "min" + ":" + seconds + "sec" + ")");

            Map<Question, Answer> questionAnswerMap1 = questionAnswerMap.get(randomness);

            for (Question question : questionAnswerMap1.keySet()) {
                BaseUtils.println(i + ") " + BaseUtils.gson.toJson(question));

                Answer answer = questionAnswerMap1.get(question);

                Answer build = Answer.childBuilder()
                        .variantA(answer.getVariantA())
                        .variantB(answer.getVariantB())
                        .variantC(answer.getVariantC()).build();

                BaseUtils.println(BaseUtils.gson.toJson(build));

                BaseUtils.println("Write answer: ", Colors.PURPLE);

                givenAnswer = BaseUtils.readText();
                if (givenAnswer.equalsIgnoreCase(answer.getCorrectAnswer())) {
                    counterOfCorrectAnswer++;
                }
                if (!givenAnswer.isEmpty()) {
                    answersNumber++;
                }
                i++;

            }

            if (endTime.isBefore(LocalDateTime.now())) {
                BaseUtils.println("Unfortunately time is over!", Colors.RED);
                long spendTime = ChronoUnit.SECONDS.between(startTime, LocalDateTime.now());
                BaseUtils.println("Actually you should have spent "
                        + Long.parseLong(quizNumber) * 30 + "seconds, but you have spent " + spendTime + " seconds.");
            } else {
                System.out.println("You solved problems on time!");
            }
            BaseUtils.println("*********  Result of test   ************", Colors.GREEN);
            BaseUtils.println("Total number of chosen questions: " + quizNumber, Colors.GREEN);
            BaseUtils.println("Total number of given answers: " + answersNumber, Colors.GREEN);
            BaseUtils.println("Total number of correct found answers: " + counterOfCorrectAnswer, Colors.GREEN);
        }
        return new Response<>(new DataVO<>(null, true));
    }

    public static SolveTestService getInstance() {
        if (instance == null) {
            instance = new SolveTestService();
        }
        return instance;
    }
}
