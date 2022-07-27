package com.github.kylecdrck.filelocker;

public record FileLockDetail(int processed, int success, int failed, String errMessage) {
    public String getMessage() {
        if (errMessage == null || errMessage.isBlank()) {
            if (success == processed) {
                return String.format("Successfully processed %d files", success);
            } else {
                return String.format("Successfully processed %d out of %d files. Completed with %d errors.", success, processed, failed);
            }
        } else {
            return errMessage;
        }
    }
}
