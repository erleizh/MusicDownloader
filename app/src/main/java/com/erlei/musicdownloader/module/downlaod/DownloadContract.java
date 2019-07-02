package com.erlei.musicdownloader.module.downlaod;

import com.erlei.musicdownloader.base.Contract;

public interface DownloadContract extends Contract {

    interface View extends Contract.View {


    }

    interface Model extends Contract.Model {


    }

    abstract class Presenter extends Contract.Presenter<DownloadContract.View> {

        public Presenter(DownloadContract.View view) {
            super(view);
        }

    }
}
