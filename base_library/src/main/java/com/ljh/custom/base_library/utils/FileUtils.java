package com.ljh.custom.base_library.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;

import com.ljh.custom.base_library.BuildConfig;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * File Utils
 * Desc: 日期时间工具类
 * Created by ${junhua.li} on 2017/06/14 14:43.
 * Email: lijunhuayc@sina.com
 */
public class FileUtils {
    private static Context mApplication;
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    public final static String FILE_CACHE_DIR = "files";
    public final static String GLIDE_CACHE_DIR = "image_manager_disk_cache";
    public final static String VOLLEY_CACHE_DIR = "volley";
    public final static String FRESCO_CACHE_DIR = "fresco_cache";
    public final static String UPLOAD_CACHE_DIR = "upload-cache";
    public final static String IMG_CACHE_DIR = "temp_images";
    public final static String COOKIE_CACHE_DIR = "temp_special";
    //
    public final static String FILE_DB_DIR = "databases";
    public final static String FILE_MY_FILES_DIR = "my_files";

    private FileUtils() {
        throw new AssertionError();
    }

    public static void init(Context mContext) {
        if (mContext instanceof Application) {
            mApplication = mContext;
        } else {
            throw new RuntimeException("FileUtils initialize must in Application.");
        }
    }

    /**
     * cache目录Path
     *
     * @return
     */
    public static String getExternalCacheDirPath() {
        return getExternalCacheDirFile().getPath();
    }

    /**
     * cache目录File
     *
     * @return
     */
    public static File getExternalCacheDirFile() {
        File cacheDirFile = mApplication.getExternalCacheDir();
        if (null == cacheDirFile) {
            cacheDirFile = mApplication.getCacheDir();
        }
        return cacheDirFile;
    }

    /**
     * cache子目录Path
     *
     * @param childDirPath
     * @return
     */
    public static String getExternalCacheDirChildPath(String childDirPath) {
        if (!TextUtils.isEmpty(childDirPath)) {
            return getExternalCacheDirPath() + File.separator + childDirPath;
        } else {
            return getExternalCacheDirPath();
        }
    }

    /**
     * @return 返回一个随机图片缓存路径
     */
    public static String getRandomImagePath() {
        return getExternalCacheDirChildPath(IMG_CACHE_DIR) + File.separator + System.currentTimeMillis() + ".jpg";
    }

    /**
     * cache子目录File
     *
     * @param childDirPath
     * @return
     */
    public static File getExternalCacheDirChildFile(String childDirPath) {
        File file = new File(getExternalCacheDirChildPath(childDirPath));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.mkdirs();
        }
        return file;
    }

    /**
     * file目录Path
     *
     * @return
     */
    public static String getExternalFileDirPath() {
        return getExternalFileDirFile().getPath();
    }

    /**
     * file目录File
     *
     * @return
     */
    public static File getExternalFileDirFile() {
        File fileDir = mApplication.getExternalFilesDir(null);
        if (null == fileDir) {
            fileDir = mApplication.getFilesDir();
        }
        return fileDir;
    }

    /**
     * file 子目录 File
     *
     * @param childDirPath
     * @return
     */
    public static String getExternalFileDirChildPath(String childDirPath) {
        if (!TextUtils.isEmpty(childDirPath)) {
            return getExternalFileDirPath() + File.separator + childDirPath;
        } else {
            return getExternalFileDirPath();
        }
    }

    /**
     * file 子目录 File
     *
     * @param childDirPath
     * @return
     */
    public static File getExternalFileDirChildFile(String childDirPath) {
        File file = new File(getExternalFileDirChildPath(childDirPath));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.mkdirs();
        }
        return file;
    }

    public static String convertFileSize(float fileSize) {
        if (fileSize > 0 && fileSize < 1000) {
            return fileSize + "B";
        } else if (fileSize >= 1000 && fileSize < 1000 * 1000) {
            return (float) ((int) (fileSize / 1024 * 100)) / 100 + "KB";
        } else if (fileSize >= 1000 * 1000 && fileSize < 1000 * 1000 * 1000) {
            return (float) ((int) (fileSize / 1024 / 1024 * 100)) / 100 + "MB";
        } else {
            return (float) ((int) (fileSize / 1024 / 1024 / 1024) * 100) / 100 + "GB";
        }
    }

    public static void openFileThirdApp(String filePath) {
        Timber.d("openFileThirdApp: path = %s", filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String mimeType = getMimeTypeFromExtension(getExtensionName(getFileName(filePath)));
        //指定可选软件类型
        //TO-DO ...可根据文件后缀名来确定
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = "*/*";
        }
        Timber.d("openFileThirdApp: mimeType = %s", mimeType);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), mimeType);
//        intent.setClassName("包名", "类名");//使用指定app打开
        mApplication.startActivity(intent);
    }

    public static String getMimeTypeFromExtension(String extension) {
        for (String[] mimeItem : MIME_MapTable) {
            if (extension.equals(mimeItem[0]))
                return mimeItem[1];
        }
        return "";
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {"3gp", "video/3gpp"},
            {"aab", "application/x-authoware-bin"},
            {"aam", "application/x-authoware-map"},
            {"aas", "application/x-authoware-seg"},
            {"ai", "application/postscript"},
            {"aif", "audio/x-aiff"},
            {"aifc", "audio/x-aiff"},
            {"aiff", "audio/x-aiff"},
            {"als", "audio/X-Alpha5"},
            {"amc", "application/x-mpeg"},
            {"ani", "application/octet-stream"},
            {"apk", "application/vnd.android.package-archive"},
            {"asc", "text/plain"},
            {"asd", "application/astound"},
            {"asf", "video/x-ms-asf"},
            {"asn", "application/astound"},
            {"asp", "application/x-asap"},
            {"asx", "video/x-ms-asf"},
            {"au", "audio/basic"},
            {"avb", "application/octet-stream"},
            {"avi", "video/x-msvideo"},
            {"awb", "audio/amr-wb"},
            {"bcpio", "application/x-bcpio"},
            {"bin", "application/octet-stream"},
            {"bld", "application/bld"},
            {"bld2", "application/bld2"},
            {"bmp", "image/bmp"},
            {"bpk", "application/octet-stream"},
            {"bz2", "application/x-bzip2"},
            {"c", "text/plain"},
            {"cal", "image/x-cals"},
            {"ccn", "application/x-cnc"},
            {"cco", "application/x-cocoa"},
            {"cdf", "application/x-netcdf"},
            {"cgi", "magnus-internal/cgi"},
            {"chat", "application/x-chat"},
            {"class", "application/octet-stream"},
            {"clp", "application/x-msclip"},
            {"cmx", "application/x-cmx"},
            {"co", "application/x-cult3d-object"},
            {"cod", "image/cis-cod"},
            {"conf", "text/plain"},
            {"cpio", "application/x-cpio"},
            {"cpp", "text/plain"},
            {"cpt", "application/mac-compactpro"},
            {"crd", "application/x-mscardfile"},
            {"csh", "application/x-csh"},
            {"csm", "chemical/x-csml"},
            {"csml", "chemical/x-csml"},
            {"css", "text/css"},
            {"cur", "application/octet-stream"},
            {"dcm", "x-lml/x-evm"},
            {"dcr", "application/x-director"},
            {"dcx", "image/x-dcx"},
            {"dhtml", "text/html"},
            {"dir", "application/x-director"},
            {"dll", "application/octet-stream"},
            {"dmg", "application/octet-stream"},
            {"dms", "application/octet-stream"},
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"dot", "application/x-dot"},
            {"dvi", "application/x-dvi"},
            {"dwf", "drawing/x-dwf"},
            {"dwg", "application/x-autocad"},
            {"dxf", "application/x-autocad"},
            {"dxr", "application/x-director"},
            {"ebk", "application/x-expandedbook"},
            {"emb", "chemical/x-embl-dl-nucleotide"},
            {"embl", "chemical/x-embl-dl-nucleotide"},
            {"eps", "application/postscript"},
            {"eri", "image/x-eri"},
            {"es", "audio/echospeech"},
            {"esl", "audio/echospeech"},
            {"etc", "application/x-earthtime"},
            {"etx", "text/x-setext"},
            {"evm", "x-lml/x-evm"},
            {"evy", "application/x-envoy"},
            {"exe", "application/octet-stream"},
            {"fh4", "image/x-freehand"},
            {"fh5", "image/x-freehand"},
            {"fhc", "image/x-freehand"},
            {"fif", "image/fif"},
            {"fm", "application/x-maker"},
            {"fpx", "image/x-fpx"},
            {"fvi", "video/isivideo"},
            {"gau", "chemical/x-gaussian-input"},
            {"gca", "application/x-gca-compressed"},
            {"gdb", "x-lml/x-gdb"},
            {"gif", "image/gif"},
            {"gps", "application/x-gps"},
            {"gtar", "application/x-gtar"},
            {"gz", "application/x-gzip"},
            {"h", "text/plain"},
            {"hdf", "application/x-hdf"},
            {"hdm", "text/x-hdml"},
            {"hdml", "text/x-hdml"},
            {"hlp", "application/winhlp"},
            {"hqx", "application/mac-binhex40"},
            {"htm", "text/html"},
            {"html", "text/html"},
            {"hts", "text/html"},
            {"ice", "x-conference/x-cooltalk"},
            {"ico", "application/octet-stream"},
            {"ief", "image/ief"},
            {"ifm", "image/gif"},
            {"ifs", "image/ifs"},
            {"imy", "audio/melody"},
            {"ins", "application/x-NET-Install"},
            {"ips", "application/x-ipscript"},
            {"ipx", "application/x-ipix"},
            {"it", "audio/x-mod"},
            {"itz", "audio/x-mod"},
            {"ivr", "i-world/i-vrml"},
            {"j2k", "image/j2k"},
            {"jad", "text/vnd.sun.j2me.app-descriptor"},
            {"jam", "application/x-jam"},
            {"jar", "application/java-archive"},
            {"java", "text/plain"},
            {"jnlp", "application/x-java-jnlp-file"},
            {"jpe", "image/jpeg"},
            {"jpeg", "image/jpeg"},
            {"jpg", "image/jpeg"},
            {"jpz", "image/jpeg"},
            {"js", "application/x-javascript"},
            {"jwc", "application/jwc"},
            {"kjx", "application/x-kjx"},
            {"lak", "x-lml/x-lak"},
            {"latex", "application/x-latex"},
            {"lcc", "application/fastman"},
            {"lcl", "application/x-digitalloca"},
            {"lcr", "application/x-digitalloca"},
            {"lgh", "application/lgh"},
            {"lha", "application/octet-stream"},
            {"lml", "x-lml/x-lml"},
            {"lmlpack", "x-lml/x-lmlpack"},
            {"log", "text/plain"},
            {"lsf", "video/x-ms-asf"},
            {"lsx", "video/x-ms-asf"},
            {"lzh", "application/x-lzh"},
            {"m13", "application/x-msmediaview"},
            {"m14", "application/x-msmediaview"},
            {"m15", "audio/x-mod"},
            {"m3u", "audio/x-mpegurl"},
            {"m3url", "audio/x-mpegurl"},
            {"m4a", "audio/mp4a-latm"},
            {"m4b", "audio/mp4a-latm"},
            {"m4p", "audio/mp4a-latm"},
            {"m4u", "video/vnd.mpegurl"},
            {"m4v", "video/x-m4v"},
            {"ma1", "audio/ma1"},
            {"ma2", "audio/ma2"},
            {"ma3", "audio/ma3"},
            {"ma5", "audio/ma5"},
            {"man", "application/x-troff-man"},
            {"map", "magnus-internal/imagemap"},
            {"mbd", "application/mbedlet"},
            {"mct", "application/x-mascot"},
            {"mdb", "application/x-msaccess"},
            {"mdz", "audio/x-mod"},
            {"me", "application/x-troff-me"},
            {"mel", "text/x-vmel"},
            {"mi", "application/x-mif"},
            {"mid", "audio/midi"},
            {"midi", "audio/midi"},
            {"mif", "application/x-mif"},
            {"mil", "image/x-cals"},
            {"mio", "audio/x-mio"},
            {"mmf", "application/x-skt-lbs"},
            {"mng", "video/x-mng"},
            {"mny", "application/x-msmoney"},
            {"moc", "application/x-mocha"},
            {"mocha", "application/x-mocha"},
            {"mod", "audio/x-mod"},
            {"mof", "application/x-yumekara"},
            {"mol", "chemical/x-mdl-molfile"},
            {"mop", "chemical/x-mopac-input"},
            {"mov", "video/quicktime"},
            {"movie", "video/x-sgi-movie"},
            {"mp2", "audio/x-mpeg"},
            {"mp3", "audio/x-mpeg"},
            {"mp4", "video/mp4"},
            {"mpc", "application/vnd.mpohun.certificate"},
            {"mpe", "video/mpeg"},
            {"mpeg", "video/mpeg"},
            {"mpg video/mpeg"},
            {"mpg4", "video/mp4"},
            {"mpga", "audio/mpeg"},
            {"mpn", "application/vnd.mophun.application"},
            {"mpp", "application/vnd.ms-project"},
            {"mps", "application/x-mapserver"},
            {"mrl", "text/x-mrml"},
            {"mrm", "application/x-mrm"},
            {"ms", "application/x-troff-ms"},
            {"msg", "application/vnd.ms-outlook"},
            {"mts", "application/metastream"},
            {"mtx", "application/metastream"},
            {"mtz", "application/metastream"},
            {"mzv", "application/metastream"},
            {"nar", "application/zip"},
            {"nbmp", "image/nbmp"},
            {"nc", "application/x-netcdf"},
            {"ndb", "x-lml/x-ndb"},
            {"ndwn", "application/ndwn"},
            {"nif", "application/x-nif"},
            {"nmz", "application/x-scream"},
            {"nokia-op-logo", "image/vnd.nok-oplogo-color"},
            {"npx", "application/x-netfpx"},
            {"nsnd", "audio/nsnd"},
            {"nva", "application/x-neva1"},
            {"oda", "application/oda"},
            {"ogg", "audio/ogg"},
            {"oom", "application/x-AtlasMate-Plugin"},
            {"pac", "audio/x-pac"},
            {"pae", "audio/x-epac"},
            {"pan", "application/x-pan"},
            {"pbm", "image/x-portable-bitmap"},
            {"pcx", "image/x-pcx"},
            {"pda", "image/x-pda"},
            {"pdb", "chemical/x-pdb"},
            {"pdf", "application/pdf"},
            {"pfr", "application/font-tdpfr"},
            {"pgm", "image/x-portable-graymap"},
            {"pict", "image/x-pict"},
            {"pm", "application/x-perl"},
            {"pmd", "application/x-pmd"},
            {"png", "image/png"},
            {"pnm", "image/x-portable-anymap"},
            {"pnz", "image/png"},
            {"pot", "application/vnd.ms-powerpoint"},
            {"ppm", "image/x-portable-pixmap"},
            {"pps", "application/vnd.ms-powerpoint"},
            {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"pqf", "application/x-cprplayer"},
            {"pqi", "application/cprplayer"},
            {"prc", "application/x-prc"},
            {"prop", "text/plain"},
            {"proxy", "application/x-ns-proxy-autoconfig"},
            {"ps", "application/postscript"},
            {"ptlk", "application/listenup"},
            {"pub", "application/x-mspublisher"},
            {"pvx", "video/x-pv-pvx"},
            {"qcp", "audio/vnd.qcelp"},
            {"qt", "video/quicktime"},
            {"qti", "image/x-quicktime"},
            {"qtif", "image/x-quicktime"},
            {"r3t", "text/vnd.rn-realtext3d"},
            {"ra", "audio/x-pn-realaudio"},
            {"ram", "audio/x-pn-realaudio"},
            {"rar", "application/x-rar-compressed"},
            {"ras", "image/x-cmu-raster"},
            {"rc", "text/plain"},
            {"rdf", "application/rdf+xml"},
            {"rf", "image/vnd.rn-realflash"},
            {"rgb", "image/x-rgb"},
            {"rlf", "application/x-richlink"},
            {"rm", "audio/x-pn-realaudio"},
            {"rmf", "audio/x-rmf"},
            {"rmm", "audio/x-pn-realaudio"},
            {"rmvb", "audio/x-pn-realaudio"},
            {"rnx", "application/vnd.rn-realplayer"},
            {"roff", "application/x-troff"},
            {"rp", "image/vnd.rn-realpix"},
            {"rpm", "audio/x-pn-realaudio-plugin"},
            {"rt", "text/vnd.rn-realtext"},
            {"rte", "x-lml/x-gps"},
            {"rtf", "application/rtf"},
            {"rtg", "application/metastream"},
            {"rtx", "text/richtext"},
            {"rv", "video/vnd.rn-realvideo"},
            {"rwc", "application/x-rogerwilco"},
            {"s3m", "audio/x-mod"},
            {"s3z", "audio/x-mod"},
            {"sca", "application/x-supercard"},
            {"scd", "application/x-msschedule"},
            {"sdf", "application/e-score"},
            {"sea", "application/x-stuffit"},
            {"sgm", "text/x-sgml"},
            {"sgml", "text/x-sgml"},
            {"sh", "application/x-sh"},
            {"shar", "application/x-shar"},
            {"shtml", "magnus-internal/parsed-html"},
            {"shw", "application/presentations"},
            {"si6", "image/si6"},
            {"si7", "image/vnd.stiwap.sis"},
            {"si9", "image/vnd.lgtwap.sis"},
            {"sis", "application/vnd.symbian.install"},
            {"sit", "application/x-stuffit"},
            {"skd", "application/x-Koan"},
            {"skm", "application/x-Koan"},
            {"skp", "application/x-Koan"},
            {"skt", "application/x-Koan"},
            {"slc", "application/x-salsa"},
            {"smd", "audio/x-smd"},
            {"smi", "application/smil"},
            {"smil", "application/smil"},
            {"smp", "application/studiom"},
            {"smz", "audio/x-smd"},
            {"snd", "audio/basic"},
            {"spc", "text/x-speech"},
            {"spl", "application/futuresplash"},
            {"spr", "application/x-sprite"},
            {"sprite", "application/x-sprite"},
            {"spt", "application/x-spt"},
            {"src", "application/x-wais-source"},
            {"stk", "application/hyperstudio"},
            {"stm", "audio/x-mod"},
            {"sv4cpio", "application/x-sv4cpio"},
            {"sv4crc", "application/x-sv4crc"},
            {"svf", "image/vnd"},
            {"svg", "image/svg-xml"},
            {"svh", "image/svh"},
            {"svr", "x-world/x-svr"},
            {"swf", "application/x-shockwave-flash"},
            {"swfl", "application/x-shockwave-flash"},
            {"t", "application/x-troff"},
            {"tad", "application/octet-stream"},
            {"talk", "text/x-speech"},
            {"tar", "application/x-tar"},
            {"taz", "application/x-tar"},
            {"tbp", "application/x-timbuktu"},
            {"tbt", "application/x-timbuktu"},
            {"tcl", "application/x-tcl"},
            {"tex", "application/x-tex"},
            {"texi", "application/x-texinfo"},
            {"texinfo", "application/x-texinfo"},
            {"tgz", "application/x-tar"},
            {"thm", "application/vnd.eri.thm"},
            {"tif", "image/tiff"},
            {"tiff", "image/tiff"},
            {"tki", "application/x-tkined"},
            {"tkined", "application/x-tkined"},
            {"toc", "application/toc"},
            {"toy", "image/toy"},
            {"tr", "application/x-troff"},
            {"trk", "x-lml/x-gps"},
            {"trm", "application/x-msterminal"},
            {"tsi", "audio/tsplayer"},
            {"tsp", "application/dsptype"},
            {"tsv", "text/tab-separated-values"},
            {"tsv", "text/tab-separated-values"},
            {"ttf", "application/octet-stream"},
            {"ttz", "application/t-time"},
            {"txt", "text/plain"},
            {"ult", "audio/x-mod"},
            {"ustar", "application/x-ustar"},
            {"uu", "application/x-uuencode"},
            {"uue", "application/x-uuencode"},
            {"vcd", "application/x-cdlink"},
            {"vcf", "text/x-vcard"},
            {"vdo", "video/vdo"},
            {"vib", "audio/vib"},
            {"viv", "video/vivo"},
            {"vivo", "video/vivo"},
            {"vmd", "application/vocaltec-media-desc"},
            {"vmf", "application/vocaltec-media-file"},
            {"vmi", "application/x-dreamcast-vms-info"},
            {"vms", "application/x-dreamcast-vms"},
            {"vox", "audio/voxware"},
            {"vqe", "audio/x-twinvq-plugin"},
            {"vqf", "audio/x-twinvq"},
            {"vql", "audio/x-twinvq"},
            {"vre", "x-world/x-vream"},
            {"vrml", "x-world/x-vrml"},
            {"vrt", "x-world/x-vrt"},
            {"vrw", "x-world/x-vream"},
            {"vts", "workbook/formulaone"},
            {"wav", "audio/x-wav"},
            {"wax", "audio/x-ms-wax"},
            {"wbmp", "image/vnd.wap.wbmp"},
            {"web", "application/vnd.xara"},
            {"wi", "image/wavelet"},
            {"wis", "application/x-InstallShield"},
            {"wm", "video/x-ms-wm"},
            {"wma", "audio/x-ms-wma"},
            {"wmd", "application/x-ms-wmd"},
            {"wmf", "application/x-msmetafile"},
            {"wml", "text/vnd.wap.wml"},
            {"wmlc", "application/vnd.wap.wmlc"},
            {"wmls", "text/vnd.wap.wmlscript"},
            {"wmlsc", "application/vnd.wap.wmlscriptc"},
            {"wmlscript", "text/vnd.wap.wmlscript"},
            {"wmv", "audio/x-ms-wmv"},
            {"wmx", "video/x-ms-wmx"},
            {"wmz", "application/x-ms-wmz"},
            {"wpng", "image/x-up-wpng"},
            {"wps", "application/vnd.ms-works"},
            {"wpt", "x-lml/x-gps"},
            {"wri", "application/x-mswrite"},
            {"wrl", "x-world/x-vrml"},
            {"wrz", "x-world/x-vrml"},
            {"ws", "text/vnd.wap.wmlscript"},
            {"wsc", "application/vnd.wap.wmlscriptc"},
            {"wv", "video/wavelet"},
            {"wvx", "video/x-ms-wvx"},
            {"wxl", "application/x-wxl"},
            {"x-gzip", "application/x-gzip"},
            {"xar", "application/vnd.xara"},
            {"xbm", "image/x-xbitmap"},
            {"xdm", "application/x-xdma"},
            {"xdma", "application/x-xdma"},
            {"xdw", "application/vnd.fujixerox.docuworks"},
            {"xht", "application/xhtml+xml"},
            {"xhtm", "application/xhtml+xml"},
            {"xhtml", "application/xhtml+xml"},
            {"xla", "application/vnd.ms-excel"},
            {"xlc", "application/vnd.ms-excel"},
            {"xll", "application/x-excel"},
            {"xlm", "application/vnd.ms-excel"},
            {"xls", "application/vnd.ms-excel"},
            {"xlt", "application/vnd.ms-excel"},
            {"xlw", "application/vnd.ms-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"xm", "audio/x-mod"},
            {"xml", "text/xml"},
            {"xmz", "audio/x-mod"},
            {"xpi", "application/x-xpinstall"},
            {"xpm", "image/x-xpixmap"},
            {"xsit", "text/xml"},
            {"xsl", "text/xml"},
            {"xul", "text/xul"},
            {"xwd", "image/x-xwindowdump"},
            {"xyz", "chemical/x-pdb"},
            {"yz1", "application/x-yz1"},
            {"z", "application/x-compress"},
            {"zac", "application/x-zaurus-zac"},
            {"zip", "application/zip"},
            {"", "*/*"}
    };

    /** %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% **/
    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (null == contentList || contentList.isEmpty()) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * move file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     * <p>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * 获取文件的扩展后缀名
     *
     * @param filename
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * get file name from path, include suffix
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? filePath : filePath.substring(filePos + 1);
    }

    /**
     * get folder name from path
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (null == filePath || filePath.trim().length() == 0) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return {@link File#}</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (filePath == null || filePath.trim().length() == 0) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (directoryPath == null || directoryPath.trim().length() == 0) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (path == null || path.trim().length() == 0) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (path == null || path.trim().length() == 0) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     *
     * */
    public static boolean write(OutputStream outputStream, String content) {
        BufferedOutputStream out = new BufferedOutputStream(outputStream, 8 * 1024);
        try {
            byte[] bufffer = content.getBytes("utf-8");
            out.write(bufffer);
            out.flush();
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * read file
     *
     * @param is
     * @param charsetName The name of a supported
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static String read(InputStream is, String charsetName) {
        StringBuilder fileContent = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStreamReader isr = new InputStreamReader(is, charsetName);
            reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
//                if (!fileContent.toString().equals("")) {
//                    fileContent.append("\r\n");
//                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static long getDirSize(File file) {
        int size = 0;
        if (file.exists()) {
            if (file.isFile()) {
                size += file.length();
            } else {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {

                    if (files[i].isFile()) {
                        size += file.length();
                    } else {
                        size += getDirSize(files[i]);
                    }
                }
            }
        } else {
            return 0;
        }
        return size;
    }

    public static long getLastMofifyDate(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            return file.lastModified();
        else
            return -1;
    }

    /**
     * Uri 转换为 path
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                long id_;
                try {
                    id_ = Long.valueOf(id);
                } catch (Exception e) {
                    id_ = 0L;
                }
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id_);
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return imageUri.toString();
    }

    public static String buildUploadFileName(String localUrl, String name) {
        String prefix = getFileExtensionName(localUrl);
        String outFileName;
        if (BuildConfig.DEBUG) {
            outFileName = "test/" + name + "." + prefix;
        } else {
            outFileName = "upload/image/" + name + "." + prefix;
        }
        return outFileName;
    }

    public static String convertPicURL(String picURL) {
        if (TextUtils.isEmpty(picURL)) {
            return picURL;
        }
        if (picURL.startsWith("http")) {
            return picURL;
        } else {
            File file = new File(picURL);
            if (file.exists()) {
                return "file://" + picURL;
            } else {
                return "http://oss2.yicheku.com.cn" + picURL;
            }
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param url 文件路径
     */
    public static String getFileExtensionName(String url) {
        File file = new File(url);
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * 构建上传文件名称
     */
    public static String buildUploadFileName(String localUrl) {
        String prefix = getFileExtensionName(localUrl);
        String outFileName;
        if (BuildConfig.DEBUG) {
            outFileName = "test/" + CommonUtils.createUUid() + "." + prefix;
        } else {
            outFileName = "upload/image/"
                    + DateFormat.format("yyyyMMdd", System.currentTimeMillis()).toString()
                    + "/normal/" + CommonUtils.createUUid() + "." + prefix;
        }
        return outFileName;
    }

    /**
     * 构建需要给服务器的地址样式
     */
    public static String buildUploadFileUrl(String url) {
        Timber.d("buildUploadFileUrl: url = %s", url);
        return "/" + url;
    }

    /**
     * 读取assets下的json文件
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * InputStream转字符串
     */
    public static String inputStream2String(InputStream stream, String charSet) {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[1024];
        try {
            for (int n; (n = stream.read(b)) != -1; ) {
                out.append(new String(b, 0, n, charSet));
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
