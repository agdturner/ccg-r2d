/*
 * Copyright 2021 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * For 2D spatial rendering.
 */
module uk.ac.leeds.ccg.r2d {

    requires transitive ch.obermuhlner.math.big;

    requires transitive uk.ac.leeds.ccg.grids;
    requires transitive uk.ac.leeds.ccg.generic;
    requires transitive uk.ac.leeds.ccg.io;
    requires transitive uk.ac.leeds.ccg.data;
    requires transitive uk.ac.leeds.ccg.math;
    requires transitive uk.ac.leeds.ccg.stats;
    requires transitive uk.ac.leeds.ccg.v2d;

    exports uk.ac.leeds.ccg.r2d.entities;
    exports uk.ac.leeds.ccg.r2d.io;
}
