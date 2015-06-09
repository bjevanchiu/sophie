package com.tj.sophie.job.helper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * Created by mbp on 6/9/15.
 */
public final class HadoopHelper {
    private FileSystem fileSystem;

    private HadoopHelper(Configuration configuration) throws IOException {
        this.fileSystem = FileSystem.get(configuration);
    }

    public static HadoopHelper create(Configuration configuration) throws IOException {
        return new HadoopHelper(configuration);
    }

    public void copyLocalToHdfs(Path local, Path hdfs) throws IOException {
        this.fileSystem.copyFromLocalFile(local, hdfs);
    }

    public void copyHdfsToLocal(Path hdfs, Path local) throws IOException {
        this.fileSystem.copyToLocalFile(hdfs, local);
    }

    public boolean delete(Path path) throws IOException {
        return this.fileSystem.delete(path, true);
    }
}
