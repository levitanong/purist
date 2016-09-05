(ns boot.new.purist
  (:require [boot.new.templates :refer [renderer name-to-path ->files]]))

(def render (renderer "purist"))

(defn purist
  "FIXME: write documentation"
  [name & args]
   (let [argset (->> args #_(clojure.string/split args #" ")
                     (map keyword)
                     (set))
         client-only (:client-only argset)
         server (not client-only)
         data {:name name
               :sanitized (name-to-path name)
               :client-only client-only
               :server server
               :om-next (:om-next argset)
               :om-now (not (:om-next argset))}]
     (println "Generating fresh 'boot new' purist project with args: " args)
     (->> [["build.boot" (render "build.boot" data)]
           ["src/cljs/{{name}}/core.cljs" (render "core.cljs" data)]
           ["src/garden/{{name}}/styles.clj" (render "styles.clj" data)]
           ["resources/index.html" (render "index.html" data)]
           #_(when server
             ["src/clj/{{sanitized}}/core.clj" (render "core.clj" data)])]
          ;; (remove nil)
          (apply ->files data))))
