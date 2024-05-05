package com.example.demo.Controllers;

import java.io.IOException;

public class MainControllerImpl extends MainController {
    @Override
    public void handleClickListView() throws IOException {

    }
    public void loaddata() throws  IOException{
        super.loadDictionaryData();
        super.loadBookmarkData();
        super.loadHistoryData();
    }
    // Triển khai các phương thức trừu tượng ở đây
}

