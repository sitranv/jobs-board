package com.jobs.sitran.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jobs.sitran.domain.JobDetail;
import com.jobs.sitran.helper.CSVHelper;
import com.jobs.sitran.model.dto.Cosine;
import com.jobs.sitran.model.dto.TFIDF;
import com.jobs.sitran.repository.JobDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class RSService {

    @Autowired
    private JobDetailRepository jobDetailRepository;

    public List<Cosine> cosineSimilarity(List<String> cvHashtags) {
        String regex = createRegex(cvHashtags);
        List<JobDetail> jobs = this.jobDetailRepository.getJobsRelatedCV(regex);

        Map<String, Double> IDF = IDF(cvHashtags, jobs);

        List<TFIDF> TF_IDF = new ArrayList<>();

        Map<String, Double> TFIDFQuery = TFIDFQuery(cvHashtags, IDF);
        log.info("Cv hashtags: " + cvHashtags.toString());
        for (JobDetail job : jobs) {
            TFIDF tf_idf = new TFIDF();
            tf_idf.setJob(job);
            tf_idf.setTf_idf(TFIDF(cvHashtags, mapDescription(job), IDF));
            TF_IDF.add(tf_idf);
        }

        List<Cosine> results = new ArrayList<>();

        Double lengthQuery = length(TFIDFQuery, cvHashtags);

        for (TFIDF tf_idf : TF_IDF) {
            Cosine temp = new Cosine();
            temp.setJob(tf_idf.getJob());
            double dotProduct = 0.0;
            for (String hashtag : cvHashtags) {
                dotProduct += tf_idf.getTf_idf().get(hashtag) * TFIDFQuery.get(hashtag);
            }
            Double cosine = dotProduct / (length(tf_idf.getTf_idf(), cvHashtags) * lengthQuery);
            temp.setMark(cosine);
            results.add(temp);
        }

        return sortDesc(results);
    }

    public Cosine cosineSimilarityForOneJob(List<String> cvHashtags, JobDetail jobDetail) {
        String regex = createRegex(cvHashtags);
        List<JobDetail> jobs = this.jobDetailRepository.getJobsRelatedCV(regex);

        Map<String, Double> IDF = IDF(cvHashtags, jobs);

        Map<String, Double> TFIDFQuery = TFIDFQuery(cvHashtags, IDF);

        TFIDF tf_idf = new TFIDF();
        tf_idf.setJob(jobDetail);
        tf_idf.setTf_idf(TFIDF(cvHashtags, mapDescription(jobDetail), IDF));

        Double lengthQuery = length(TFIDFQuery, cvHashtags);

        Cosine temp = new Cosine();
        temp.setJob(tf_idf.getJob());
        double dotProduct = 0.0;
        for (String hashtag : cvHashtags) {
            dotProduct += tf_idf.getTf_idf().get(hashtag) * TFIDFQuery.get(hashtag);
        }
        Double cosine = dotProduct / (length(tf_idf.getTf_idf(), cvHashtags) * lengthQuery);
        temp.setMark(cosine);

        return temp;
    }

    private Map<String, Double> IDF(List<String> cv, List<JobDetail> jobs) {
        Map<String, Double> idf = new HashMap<>();
        int size = jobs.size();
        for (String hashtag : cv) {
            int count = 0;
            for (JobDetail job : jobs) {
                if (mapDescription(job).toUpperCase().contains(hashtag)) {
                    count++;
                }
            }
            idf.put(hashtag, Math.log(size / (1 + count)));
        }

        return idf;
    }

    private Map<String, Double> TFIDF(List<String> cv, String document, Map<String, Double> IDF) {
        Map<String, Double> maps = new HashMap<>();
        document = document.toUpperCase().replace("\n", " ");
        final double max = Arrays.stream(document.trim().split(" ")).count();
        for (String hashtag : cv) {
            double count = Arrays.stream(document.split(" " + hashtag + "[,. ]+| " + hashtag + "$")).count() - 1;
            maps.put(hashtag, Math.round((count / max) * IDF.get(hashtag) * 10000.0) / 10000.0);
        }

        return maps;
    }

    private Map<String, Double> TFIDFQuery(List<String> cv, Map<String, Double> IDF) {
        Map<String, Double> maps = new HashMap<>();
        final double size = cv.size();
        for (String hashtag : cv) {
            maps.put(hashtag, Math.round((1 / size) * IDF.get(hashtag) * 10000.0) / 10000.0);
        }

        return maps;
    }

    private Double length(Map<String, Double> vector, List<String> cvHashtags) {
        double queryLength = 0.0;
        for (String hashtag : cvHashtags) {
            queryLength += Math.pow(vector.get(hashtag), 2);
        }
        queryLength = Math.sqrt(queryLength);
        return queryLength;
    }

    private List<Cosine> sortDesc(List<Cosine> results) {
        for (int i = 0; i < results.size() - 1; i++) {
            for (int j = i + 1; j < results.size(); j++) {
                if (results.get(i).getMark() < results.get(j).getMark()) {
                    Cosine temp = results.get(i);
                    results.set(i, results.get(j));
                    results.set(j, temp);
                }
            }
        }
        return results;
    }

    public List<String> extractHashtags(MultipartFile cv) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("hashtags.csv");
        List<String> hashtags = CSVHelper.getHashtagsFromCSV1(is);
        String regex = createRegex(hashtags);
        Pattern pattern = Pattern.compile(regex);
        File cvFile = convert(cv);
        PDDocument document = PDDocument.load(cvFile);
        List<String> hashtagArray = new ArrayList<>();
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            Matcher matcher = pattern.matcher(text.toUpperCase().replace("/", "\\\\")
                    .replace("\n", "  "));
            while (matcher.find()) {
                hashtagArray.add(matcher.group().replace(".", "")
                        .replace(",", "").trim());
            }
        }
        document.close();
        Boolean status = cvFile.delete();
        log.info("Cv hashtags before: " + hashtagArray.toString());
        return Lists.newArrayList(Sets.newHashSet(hashtagArray));
    }

    private static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String createRegex(List<String> cvHashtags) {
        StringBuilder regex = new StringBuilder("/");
        for (int i = 0; i < cvHashtags.size(); i++) {
            String hashtag = cvHashtags.get(i).replace("/", "\\\\");
            regex.append("[^\\S]").append(hashtag).append("[,. ]|").append("[^\\S]").append(hashtag)
                    .append(i != cvHashtags.size() - 1 ? "$|" : "$");
        }

        return regex.toString();
    }

    private String mapDescription(JobDetail job) {
        String description = job.getRequest()+ " "
                + job.getHashtags().toString()+ " " + job.getTitle() + " " + job.getDescription() + " ";
        return description.replace("\n", " ");
    }
}
