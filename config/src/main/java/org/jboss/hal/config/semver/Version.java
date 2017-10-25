/*
 * The MIT License
 *
 * Copyright 2012-2015 Zafar Khaja <zafarkhaja@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jboss.hal.config.semver;

import java.util.Comparator;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Represents a version consisting of a major, minor and micro part.
 *
 * @author Zafar Khaja <zafarkhaja@gmail.com>
 * @since 0.1.0
 */
@JsType(namespace = "hal.config")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Version implements Comparable<Version> {

    @JsIgnore
    public static final Version UNDEFINED = Version.valueOf("0.0.0-undefined"); //NON-NLS

    /** The normal version. */
    private final NormalVersion normal;

    /** The pre-release version. */
    private final MetadataVersion preRelease;

    /** The build metadata. */
    private final MetadataVersion build;

    /** A separator that separates the pre-release version from the normal version. */
    private static final String PRE_RELEASE_PREFIX = "-";

    /** A separator that separates the build metadata from the normal version or the pre-release version. */
    private static final String BUILD_PREFIX = "+";

    /** A comparator that respects the build metadata when comparing versions. */
    private static final Comparator<Version> BUILD_AWARE_ORDER = new BuildAwareOrder();

    /**
     * Creates a new version as a result of parsing the specified version string.
     *
     * @param version The version string to parse
     *
     * @return a new version
     */
    public static Version valueOf(String version) {
        return VersionParser.parseValidSemVer(version);
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     * @since 0.7.0
     */
    @JsIgnore
    public static Version forIntegers(int major) {
        return new Version(new NormalVersion(major, 0, 0));
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     * @since 0.7.0
     */
    @JsIgnore
    public static Version forIntegers(int major, int minor) {
        return new Version(new NormalVersion(major, minor, 0));
    }

    /**
     * Creates a new instance of {@code Version}
     * for the specified version numbers.
     *
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException if a negative integer is passed
     * @since 0.7.0
     */
    @JsIgnore
    public static Version forIntegers(int major, int minor, int patch) {
        return new Version(new NormalVersion(major, minor, patch));
    }


    /**
     * Constructs a {@code Version} instance with the normal version.
     *
     * @param normal the normal version
     */
    private Version(NormalVersion normal) {
        this(normal, MetadataVersion.NULL, MetadataVersion.NULL);
    }

    /**
     * Constructs a {@code Version} instance with the
     * normal version and the pre-release version.
     *
     * @param normal     the normal version
     * @param preRelease the pre-release version
     */
    private Version(NormalVersion normal, MetadataVersion preRelease) {
        this(normal, preRelease, MetadataVersion.NULL);
    }

    /**
     * Constructs a {@code Version} instance with the normal
     * version, the pre-release version and the build metadata.
     *
     * @param normal     the normal version
     * @param preRelease the pre-release version
     * @param build      the build metadata
     */
    Version(NormalVersion normal, MetadataVersion preRelease, MetadataVersion build) {
        this.normal = normal;
        this.preRelease = preRelease;
        this.build = build;
    }

    /**
     * Increments the major version.
     *
     * @return a new instance of the {@code Version} class
     */
    @JsIgnore
    public Version incrementMajorVersion() {
        return new Version(normal.incrementMajor());
    }

    /**
     * Increments the major version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    @JsIgnore
    public Version incrementMajorVersion(String preRelease) {
        return new Version(normal.incrementMajor(), VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Increments the minor version.
     *
     * @return a new instance of the {@code Version} class
     */
    @JsIgnore
    public Version incrementMinorVersion() {
        return new Version(normal.incrementMinor());
    }

    /**
     * Increments the minor version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    @JsIgnore
    public Version incrementMinorVersion(String preRelease) {
        return new Version(normal.incrementMinor(), VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Increments the patch version.
     *
     * @return a new instance of the {@code Version} class
     */
    @JsIgnore
    public Version incrementPatchVersion() {
        return new Version(normal.incrementPatch());
    }

    /**
     * Increments the patch version and appends the pre-release version.
     *
     * @param preRelease the pre-release version to append
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    @JsIgnore
    public Version incrementPatchVersion(String preRelease) {
        return new Version(normal.incrementPatch(), VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Increments the pre-release version.
     *
     * @return a new instance of the {@code Version} class
     */
    @JsIgnore
    public Version incrementPreReleaseVersion() {
        return new Version(normal, preRelease.increment());
    }

    /**
     * Increments the build metadata.
     *
     * @return a new instance of the {@code Version} class
     */
    @JsIgnore
    public Version incrementBuildMetadata() {
        return new Version(normal, preRelease, build.increment());
    }

    /**
     * Sets the pre-release version.
     *
     * @param preRelease the pre-release version to set
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    @JsIgnore
    public Version setPreReleaseVersion(String preRelease) {
        return new Version(normal, VersionParser.parsePreRelease(preRelease));
    }

    /**
     * Sets the build metadata.
     *
     * @param build the build metadata to set
     *
     * @return a new instance of the {@code Version} class
     *
     * @throws IllegalArgumentException     if the input string is {@code NULL} or empty
     * @throws ParseException               when invalid version string is provided
     * @throws UnexpectedCharacterException is a special case of {@code ParseException}
     */
    @JsIgnore
    public Version setBuildMetadata(String build) {
        return new Version(normal, preRelease, VersionParser.parseBuild(build));
    }

    /**
     * @return the major version number.
     */
    @JsProperty(name = "major")
    public int getMajorVersion() {
        return normal.getMajor();
    }

    /**
     * @return the minor version number.
     */
    @JsProperty(name = "minor")
    public int getMinorVersion() {
        return normal.getMinor();
    }

    /**
     * @return the patch version number.
     */
    @JsProperty(name = "micro")
    public int getPatchVersion() {
        return normal.getPatch();
    }

    /**
     * Returns the string representation of the normal version.
     *
     * @return the string representation of the normal version
     */
    @JsIgnore
    public String getNormalVersion() {
        return normal.toString();
    }

    /**
     * Returns the string representation of the pre-release version.
     *
     * @return the string representation of the pre-release version
     */
    @JsIgnore
    public String getPreReleaseVersion() {
        return preRelease.toString();
    }

    /**
     * Returns the string representation of the build metadata.
     *
     * @return the string representation of the build metadata
     */
    @JsIgnore
    public String getBuildMetadata() {
        return build.toString();
    }

    /**
     * Checks if this version is greater than the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is greater than the other version
     * or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    @JsIgnore
    public boolean greaterThan(Version other) {
        return compareTo(other) > 0;
    }

    /**
     * Checks if this version is greater than or equal to the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is greater than or equal
     * to the other version or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    @JsIgnore
    public boolean greaterThanOrEqualTo(Version other) {
        return compareTo(other) >= 0;
    }

    /**
     * Checks if this version is less than the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is less than the other version
     * or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    @JsIgnore
    public boolean lessThan(Version other) {
        return compareTo(other) < 0;
    }

    /**
     * Checks if this version is less than or equal to the other version.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version is less than or equal
     * to the other version or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    @JsIgnore
    public boolean lessThanOrEqualTo(Version other) {
        return compareTo(other) <= 0;
    }

    /**
     * Checks if this version equals the other version.
     * <p>
     * The comparison is done by the {@code Version.compareTo} method.
     *
     * @param other the other version to compare to
     *
     * @return {@code true} if this version equals the other version
     * or {@code false} otherwise
     *
     * @see #compareTo(Version other)
     */
    @Override
    @JsIgnore
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Version)) {
            return false;
        }
        return compareTo((Version) other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsIgnore
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + normal.hashCode();
        hash = 97 * hash + preRelease.hashCode();
        return hash;
    }

    /**
     * @return &lt;major&gt;.&lt;minor&gt;.&lt;micro&gt;
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getNormalVersion());
        if (!getPreReleaseVersion().isEmpty()) {
            sb.append(PRE_RELEASE_PREFIX).append(getPreReleaseVersion());
        }
        if (!getBuildMetadata().isEmpty()) {
            sb.append(BUILD_PREFIX).append(getBuildMetadata());
        }
        return sb.toString();
    }

    /**
     * Compares this version to the other version.
     * <p>
     * This method does not take into account the versions' build
     * metadata. If you want to compare the versions' build metadata
     * use the {@code Version.compareWithBuildsTo} method or the
     * {@code Version.BUILD_AWARE_ORDER} comparator.
     *
     * @param other the other version to compare to
     *
     * @return a negative integer, zero or a positive integer if this version
     * is less than, equal to or greater the the specified version
     *
     * @see #BUILD_AWARE_ORDER
     */
    @Override
    @JsIgnore
    public int compareTo(Version other) {
        int result = normal.compareTo(other.normal);
        if (result == 0) {
            result = preRelease.compareTo(other.preRelease);
        }
        return result;
    }


    /** A mutable builder for the immutable {@code Version} class. */
    public static class Builder {

        /** The normal version string. */
        private String normal;

        /** The pre-release version string. */
        private String preRelease;

        /** The build metadata string. */
        private String build;

        /** Constructs a {@code Builder} instance. */
        public Builder() {
        }

        /**
         * Constructs a {@code Builder} instance with the string representation of the normal version.
         *
         * @param normal the string representation of the normal version
         */
        public Builder(String normal) {
            this.normal = normal;
        }

        /**
         * Sets the normal version.
         *
         * @param normal the string representation of the normal version
         *
         * @return this builder instance
         */
        public Builder setNormalVersion(String normal) {
            this.normal = normal;
            return this;
        }

        /**
         * Sets the pre-release version.
         *
         * @param preRelease the string representation of the pre-release version
         *
         * @return this builder instance
         */
        public Builder setPreReleaseVersion(String preRelease) {
            this.preRelease = preRelease;
            return this;
        }

        /**
         * Sets the build metadata.
         *
         * @param build the string representation of the build metadata
         *
         * @return this builder instance
         */
        public Builder setBuildMetadata(String build) {
            this.build = build;
            return this;
        }

        /**
         * Builds a {@code Version} object.
         *
         * @return a newly built {@code Version} instance
         *
         * @throws ParseException               when invalid version string is provided
         * @throws UnexpectedCharacterException is a special case of {@code ParseException}
         */
        public Version build() {
            StringBuilder sb = new StringBuilder();
            if (isFilled(normal)) {
                sb.append(normal);
            }
            if (isFilled(preRelease)) {
                sb.append(PRE_RELEASE_PREFIX).append(preRelease);
            }
            if (isFilled(build)) {
                sb.append(BUILD_PREFIX).append(build);
            }
            return VersionParser.parseValidSemVer(sb.toString());
        }

        /**
         * Checks if a string has a usable value.
         *
         * @param str the string to check
         *
         * @return {@code true} if the string is filled or {@code false} otherwise
         */
        private boolean isFilled(String str) {
            return str != null && !str.isEmpty();
        }
    }


    /** A build-aware comparator. */
    private static class BuildAwareOrder implements Comparator<Version> {

        /**
         * Compares two {@code Version} instances taking
         * into account their build metadata.
         * <p>
         * When compared build metadata is divided into identifiers. The
         * numeric identifiers are compared numerically, and the alphanumeric
         * identifiers are compared in the ASCII sort order.
         * <p>
         * If one of the compared versions has no defined build
         * metadata, this version is considered to have a lower
         * precedence than that of the other.
         *
         * @return {@inheritDoc}
         */
        @Override
        public int compare(Version v1, Version v2) {
            int result = v1.compareTo(v2);
            if (result == 0) {
                result = v1.build.compareTo(v2.build);
                if (v1.build == MetadataVersion.NULL ||
                        v2.build == MetadataVersion.NULL
                        ) {
                    /*
                     * Build metadata should have a higher precedence
                     * than the associated normal version which is the
                     * opposite compared to pre-release versions.
                     */
                    result = -1 * result;
                }
            }
            return result;
        }
    }
}