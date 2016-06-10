package me.childintime.childintime.util.file;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class PathUtils {

    /**
     * Check whether a path is valid and parsable, without the file needing to exist.
     *
     * @param path Path to validate.
     *
     * @return True if the path is valid, false if not.
     */
    public static boolean isValidPath(String path) {
        // TODO: Is this indeed true, does the file not need to exist?

        // Validate the path
        try {
            // Try to parse the given path as string representation into a path instance
            Paths.get(path);

            // The path seems to be valid
            return true;

        } catch(Exception ignored) { }

        // The path seems to be invalid, return false
        return false;
    }

    /**
     * Get the relative path from one file to another, specifying the directory separator.
     * If one of the provided resources does not exist, it is assumed to be a file unless it ends with '/' or '\'.
     *
     * @param targetPath targetPath is calculated to this file
     * @param basePath basePath is calculated from this file
     * @param pathSeparator directory separator. The platform default is not assumed so that we can test Unix behaviour when running on Windows (for example)
     *
     * @return The relative path.
     */
    // TODO: Update the docs for this method!
    public static String getRelativePath(String targetPath, String basePath, String pathSeparator) {
        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        switch(pathSeparator) {
        case "/":
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);
            break;

        case "\\":
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);
            break;

        default:
            throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuilder common = new StringBuilder();

        int commonIndex = 0;
        while(commonIndex < target.length && commonIndex < base.length && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex]).append(pathSeparator);
            commonIndex++;
        }

        if(commonIndex == 0)
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new PathResolutionException("No common path element found for '" + normalizedTargetPath + "' and '" + normalizedBasePath + "'");

        // The number of directories we have to backtrack depends on whether the base is a file or a dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        // 
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if(baseResource.exists())
            baseIsFile = baseResource.isFile();
        else if(basePath.endsWith(pathSeparator))
            baseIsFile = false;

        StringBuilder relative = new StringBuilder();

        if(base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for(int i = 0; i < numDirsUp; i++)
                relative.append("..").append(pathSeparator);
        }

        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }

    // TODO: Is this required?
    static class PathResolutionException extends RuntimeException {
        PathResolutionException(String msg) {
            super(msg);
        }
    }
}