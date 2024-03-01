package com.sparta.lv3backoffice.global.exception;


// getDescription 메소드를 사용해서 상태코드 사용
public class StatusCode {

    // 200 OK
    public static final int OK = 200;

    // 201 Created
    public static final int CREATED = 201;

    // 400 Bad Request
    public static final int BAD_REQUEST = 400;

    // 401 Unauthorized
    public static final int UNAUTHORIZED = 401;

    // 404 Not Found
    public static final int NOT_FOUND = 404;

    // 500 Internal Server Error
    public static final int INTERNAL_SERVER_ERROR = 500;

    // 각 상태 코드에 대한 설명을 포함할 수 있는 메소드 추가
    public static String getDescription(int statusCode) {
        switch (statusCode) {
            case OK:
                return "OK - 요청이 성공적으로 처리되었습니다.";
            case CREATED:
                return "Created - 새로운 리소스가 생성되었습니다.";
            case BAD_REQUEST:
                return "Bad Request - 요청이 잘못되었습니다.";
            case UNAUTHORIZED:
                return "Unauthorized - 인증이 필요합니다.";
            case NOT_FOUND:
                return "Not Found - 요청한 리소스를 찾을 수 없습니다.";
            case INTERNAL_SERVER_ERROR:
                return "Internal Server Error - 서버에서 오류가 발생했습니다.";
            default:
                return "Unknown status code";
        }
    }
}


// 이렇게 사용하세요.

// 예시
// public class Main {
//    public static void main(String[] args) {
//        int statusCode = 404; // 예시로 404 상태 코드를 사용합니다.
//
//        // 상태 코드에 대한 설명을 출력합니다.
//        System.out.println("상태 코드: " + statusCode);
//        System.out.println("설명: " + StatusCode.getDescription(statusCode));
//    }
//}

// 실행 결과
// 상태 코드: 404
// 설명: Not Found - 요청한 리소스를 찾을 수 없습니다.