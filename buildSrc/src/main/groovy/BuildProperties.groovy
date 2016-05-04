class BuildProperties {

    private final Properties properties
    private final URI uri
    private final BuildProperties defaults
    private final def project

    BuildProperties(File path, def project) {
        this.project = project
        this.uri = URI.create(path.getAbsolutePath())
        this.properties = new Properties()
        this.properties.load(new FileInputStream(path))
        if (properties.getProperty('extends') != null) {
            this.defaults = getProperties('extends')
        } else {
            this.defaults = null
        }
    }

    int getInt(String property) {
        checkPropertyOrThrow(property)
        Integer.parseInt(getPropertyValue(property))
    }

    double getDouble(String property) {
        checkPropertyOrThrow(property)
        Double.parseDouble(getPropertyValue(property))
    }

    String getString(String property) {
        checkPropertyOrThrow(property)
        getPropertyValue(property)
    }

    File getFile(String property) {
        String filePath = getString(property)
        if (URI.create(filePath).getScheme() != null) {
            new File(filePath)
        } else {
            new File(uri.resolve(filePath).toString())
        }
    }

    BuildProperties getProperties(String property) {
        checkPropertyOrThrow(property)
        new BuildProperties(getFile(property), project)
    }

    private void checkPropertyOrThrow(String property) {
        if (getPropertyValue(property) == null) {
            throw new RuntimeException("Please define correct value for \"${property}\"")
        }
    }

    boolean contains(String name) {
        return getPropertyValue(name)
    }

    private String getPropertyValue(String name) {
        if (project.hasProperty(name)) {
            return project.getProperty(name)
        }

        if (defaults != null) {
            return properties.getProperty(name, defaults.getPropertyValue(name))
        }

        return properties.getProperty(name, null)
    }

    String getAbsolutePath() {
        uri.toString()
    }

    boolean equals(o) {
        if (this.is(o)) {
            return true
        }

        if (!(o instanceof BuildProperties)) {
            return false
        }

        BuildProperties that = (BuildProperties) o
        uri.equals(that.uri)
    }

    static BuildProperties load(String path, def project) {
        String fullPath
        if (path.startsWith("/")) {
            File rootDir = project.projectDir
            fullPath = rootDir.getParent() + path
        } else {
            fullPath = path
        }
        File file = new File(fullPath)
        if (!file.exists()) {
            throw new RuntimeException("You need to put a property file containing the configuration for the app in ${file.getAbsolutePath()}")
        }
        new BuildProperties(file, project)
    }

}
