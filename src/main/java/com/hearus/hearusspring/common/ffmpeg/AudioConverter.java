package com.hearus.hearusspring.common.ffmpeg;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class AudioConverter {

    public byte[] convertAudio(byte[] decodedBytes) throws Exception {
        // Create an input stream from the decoded bytes
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);

        // Create a temporary file to store the audio data
        File tempInputFile = File.createTempFile("temp_audio_input", ".webm");
        tempInputFile.deleteOnExit();

        // Write the audio data from the InputStream to the temporary file
        try (FileOutputStream fos = new FileOutputStream(tempInputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        // Create a temporary file to store the converted audio data
        File tempOutputFile = File.createTempFile("temp_audio_output", ".raw");
        tempOutputFile.deleteOnExit();

        // Use FFmpeg to convert the audio data
        FFmpeg ffmpeg = new FFmpeg("src/main/resources/ffmpeg/bin/ffmpeg");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(tempInputFile.getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(tempOutputFile.getAbsolutePath())
                .setAudioCodec("pcm_s16le")
                .setAudioChannels(1)
                .setAudioSampleRate(16000)
                .setFormat("s16le")
                .done();

        // Execute the FFmpeg command
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        FFmpegJob job = executor.createJob(builder);

        job.run();

        // Read the converted audio data from the temporary output file
        byte[] convertedBytes;
        try (FileInputStream fis = new FileInputStream(tempOutputFile)) {
            convertedBytes = fis.readAllBytes();
        }

        return convertedBytes;
    }
}
